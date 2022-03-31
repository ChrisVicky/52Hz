package com.example._52hz.controller;

import com.example._52hz.service.UserService;
import com.example._52hz.util.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: _52Hz
 * @description: User Controller --> For user Issue
 * @author: Christopher Liu
 * @create: 2022-03-31 16:02
 */
@RestController
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/api/getUserByStuNumber")
    public APIResponse getUserByStuNumber(@RequestParam("stuNumber") String stuNumber){
        return userService.getUserByStuNumber(stuNumber);
    }
}
