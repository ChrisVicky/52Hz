package com.example._52hz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example._52hz.config.RestTemplateConfig;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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

    private ReentrantLock lock = new ReentrantLock();


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

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse tokenLogin(String token){
        lock.lock();
        try{

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            Request request =
                    new Request.Builder()
                            .url(RestTemplateConfig.getSingle_url())
                            .addHeader("domain", RestTemplateConfig.getDomain())
                            .addHeader("ticket", RestTemplateConfig.getTicket())
                            .addHeader("token", token)
                            .build();

            Response response = client.newCall(request).execute();
            String body = response.body().string();
            System.out.println(body);
            JSONObject jsonObject = JSONObject.parseObject(body).getJSONObject("result");
            System.out.println(jsonObject);
            response.close();
            TwtUser twtUser = JSON.toJavaObject(jsonObject, TwtUser.class);
//            System.out.println(twtUser.getRealname());
            return APIResponse.success(twtUser);
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }
}
