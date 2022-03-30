package com.example._52hz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example._52hz.config.RestTemplateConfig;
import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.User;
import com.example._52hz.service.LogService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.TwtLoginResponse;
import com.example._52hz.util.TwtUser;
import okhttp3.*;
import org.apache.ibatis.jdbc.Null;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.genid.GenId;

import javax.annotation.Resource;
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
    UserMapper userMapper;

    private ReentrantLock lock = new ReentrantLock();

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

    private void loadUser(TwtUser twtUser){
        List<User> userList = userMapper.getUserByStuNumber(twtUser.getUserNumber());
        // convert into grade
        String grade = twtUser.getStuType().substring(1,1) + "20" + (twtUser.getUserNumber().substring(2,3));
        // convert Date
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if(userList.isEmpty()){
            // New here
            userMapper.insertNewUser(
                    twtUser.getUserNumber(),    twtUser.getRealname(),
                    grade,                      twtUser.getGender(),
                    twtUser.getTelephone(),     twtUser.getEmail(),
                    ft.format(date),            ft.format(date),
                    twtUser.getMajor(),         twtUser.getDepartment(),
                    twtUser.getCampus(), 0);
        }else {
            // Update user information
            userMapper.updateUserWhenLogin(
                    twtUser.getMajor(), twtUser.getDepartment(),
                    twtUser.getCampus(), twtUser.getTelephone(),
                    twtUser.getEmail(), grade,
                    twtUser.getUserNumber(), ft.format(date));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse tokenLogin(String token){
        lock.lock();
        try{
            TwtUser twtUser = httpGetUserWithToken(token);

            if(twtUser==null){
                return APIResponse.error(ErrorCode.TOKEN_LOGIN_ERROR);
            }

            loadUser(twtUser);

            return APIResponse.success(userMapper.getUserByStuNumber(twtUser.getUserNumber()));
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }
}
