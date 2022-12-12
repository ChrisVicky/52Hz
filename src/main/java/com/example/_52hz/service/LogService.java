package com.example._52hz.service;

import com.example._52hz.util.APIResponse;

import javax.servlet.http.HttpSession;

/**
 * @author Christopher
 * @version 1.0
 */
public interface LogService {
    APIResponse tokenLogin(String token, HttpSession httpSession);

    APIResponse classicLogin(String account, String password, HttpSession httpSession);

    APIResponse logBack(String stu_number, HttpSession httpSession);
}
