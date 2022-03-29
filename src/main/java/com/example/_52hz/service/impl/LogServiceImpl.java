package com.example._52hz.service.impl;

import com.example._52hz.config.RestTemplateConfig;
import com.example._52hz.service.LogService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.TwtLoginResponse;
import com.example._52hz.util.TwtUser;
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
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("token", token);
            HttpHeaders headers = new HttpHeaders();
            headers.set("ticket", RestTemplateConfig.getTicket());
            headers.set("domain", RestTemplateConfig.getDomain());
            TwtLoginResponse response = restTemplate.postForObject("https://api.twt.edu.cn/api/user/single"
                    , new HttpEntity<>(map, headers), TwtLoginResponse.class, map);
            if(response.getError_code() == 0){
                TwtUser twtUser = getTwtUserFromMap((LinkedHashMap<String, String>) response.getResult());
                String userNumber = twtUser.getUserNumber();
                int grade = 2000 + (userNumber.charAt(2) - '0') * 10 + (userNumber.charAt(3) - '0');

                // 获取当前时间
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            }
        }
    }
}
