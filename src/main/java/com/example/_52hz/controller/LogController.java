package com.example._52hz.controller;

import com.example._52hz.service.LogService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.IpHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @program: _52Hz
 * @description: Deal with Login and Logout
 * @author: Christopher Liu
 * @create: 2022-03-31 15:25
 */
@RestController
public class LogController {
    @Resource
    LogService logService;

    @Resource
    HttpServletRequest httpServletRequest;

    @PostMapping("/api/token/login")
    public APIResponse tokenLogin(@RequestParam("token") String token,
                                  HttpSession httpSession) throws IOException {
        return logService.tokenLogin(token, httpSession);
    }

    @PostMapping("/api/login")
    public APIResponse login(@RequestParam("account") String account,
                             @RequestParam("password") String password,
                             HttpSession httpSession) throws IOException{
        return logService.classicLogin(account, password, httpSession);
    }

}
