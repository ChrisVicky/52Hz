package com.example._52hz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example._52hz.config.RestTemplateConfig;
import com.example._52hz.dao.FMsgMapper;
import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.User;
import com.example._52hz.service.LogService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.TwtLoginResponse;
import com.example._52hz.util.TwtUser;
import okhttp3.*;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: _52Hz
 * @description: Login Impl
 * @author: Christopher Liu
 * @create: 2022-03-29 19:59
 */
@Service
public class LogServiceImpl implements LogService {

    @Resource
    RestTemplate restTemplate;

    @Resource
    FMsgMapper fMsgMapper;

    @Resource
    UserMapper userMapper;

    private ReentrantLock lock = new ReentrantLock();

    // Log in Through Token with Get Method.
    private TwtUser httpGetUserWithToken(String token) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            // Set Header,
            // Required Method --> GET
            Request request =
                    new Request.Builder()
                            .url(RestTemplateConfig.getSingle_url())
                            .addHeader("domain", RestTemplateConfig.getDomain())
                            .addHeader("ticket", RestTemplateConfig.getTicket())
                            .addHeader("token", token)
                            .build();
            Response response = client.newCall(request).execute();
            if(response.body()==null){
                return null;
            }
            String body = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(body).getJSONObject("result");
            response.close();
            // String --> to Json  --> to TwtUser
            return JSON.toJavaObject(jsonObject, TwtUser.class);
        }catch (Exception e){
            return null;
        }
    }

    // Load User from TwtUser to Our User. --> Update or Insert Database
    private User loadUser(TwtUser twtUser){
        List<User> userList = userMapper.getUserByStuNumber(twtUser.getUserNumber());
        // convert into grade
        String grade = twtUser.getStuType().charAt(0) + "20" + (twtUser.getUserNumber().substring(2,4));
        // convert Date
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(userList.isEmpty()){
            // New here
            userMapper.insertNewUser(
                    twtUser.getUserNumber(),    twtUser.getRealname(),
                    grade,                      twtUser.getGender(),
                    twtUser.getTelephone(),     twtUser.getEmail(),
                    ft.format(date),            ft.format(date),
                    twtUser.getMajor(),         twtUser.getDepartment(),
                    twtUser.getCampus(), 0);
            // UPDATE USER FMSG
            User user = userMapper.getUserByStuNumber(twtUser.getUserNumber()).get(0);
            if(user.getWechat()!=null){
                fMsgMapper.updateRIdWithWechat(user.getU_id(), user.getWechat());
            }
            if(user.getStu_number()!=null){
                fMsgMapper.updateRIdWithStuNumber(user.getU_id(), user.getStu_number());
            }
            if(user.getPhone()!=null){
                fMsgMapper.updateRIdWithPhone(user.getU_id(), user.getPhone());
            }
        }else {
            // Update user information
            userMapper.updateUserWhenLogin(
                    twtUser.getMajor(), twtUser.getDepartment(),
                    twtUser.getCampus(), twtUser.getTelephone(),
                    twtUser.getEmail(), grade,
                    twtUser.getUserNumber(), ft.format(date));
        }

        return userMapper.getUserByStuNumber(twtUser.getUserNumber()).get(0);
    }

    // Get User Information From Request.getResult()
    private TwtUser getTwtUserFromMap(LinkedHashMap<String, String> map) {
        TwtUser user = new TwtUser();
        user.setUserNumber(map.get("userNumber"));
        user.setEmail(map.get("email"));
        user.setGender(map.get("gender"));
        user.setIdNumber(map.get("idNumber"));
        user.setMajor(map.get("major"));
        user.setNickname(map.get("nickname"));
        user.setRealname(map.get("realname"));
        user.setRole(map.get("role"));
        user.setStuType(map.get("stuType"));
        user.setTelephone(map.get("telephone"));
        user.setToken(map.get("token"));
        user.setAvatar(map.get("avatar"));
        user.setCampus(map.get("campus"));
        user.setDepartment(map.get("department"));
        return user;
    }

    // Login with Account and Password Through POST Method.
    private TwtLoginResponse postLogin(String account, String password){
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("account", account);
        map.add("password", password);
        HttpHeaders headers = new HttpHeaders();
        headers.set("ticket", RestTemplateConfig.getTicket());
        headers.set("domain", RestTemplateConfig.getDomain());
        TwtLoginResponse response = restTemplate.postForObject(
                RestTemplateConfig.getPost_url(),
                new HttpEntity<>(map, headers),
                TwtLoginResponse.class,
                map);
        return response;
    }


    /**
     * Login with Token
     * @param token
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse tokenLogin(String token, HttpSession httpSession){
        lock.lock();
        try{
            TwtUser twtUser = httpGetUserWithToken(token);

            if(twtUser==null){
                return APIResponse.error(ErrorCode.TOKEN_LOGIN_ERROR);
            }

            User user = loadUser(twtUser);
            httpSession.setAttribute("user", user);
            return APIResponse.success(user);
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }


    /**
     * Login with Account and Password
     * @param account
     * @param password
     * @param httpSession
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse classicLogin(String account, String password, HttpSession httpSession){
        lock.lock();
        try{
            TwtLoginResponse response = postLogin(account, password);
            if(response.getError_code()==0){

                // CORRECT!
                TwtUser twtUser = getTwtUserFromMap((LinkedHashMap<String, String>) response.getResult());
                User user = loadUser(twtUser);
                httpSession.setAttribute("user", user);
                return APIResponse.success(twtUser);

            }else if (response.getError_code() == 40001) {

                return APIResponse.error(ErrorCode.LOGIN_ERROR);

            } else if (response.getError_code() == 40002) {

                return APIResponse.error(ErrorCode.NO_SUCH_USER);

            } else if (response.getError_code() == 40004) {

                return APIResponse.error(ErrorCode.PASSWORD_ERROR);

            } else

                return APIResponse.error(response.getError_code(), response.getMessage());

        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse logBack(String stu_number, HttpSession httpSession) {
        User user = userMapper.getUserByStuNumber(stu_number).get(0);
        if(user==null){
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
        httpSession.setAttribute("user", user);
        return APIResponse.success(user);
    }

}
