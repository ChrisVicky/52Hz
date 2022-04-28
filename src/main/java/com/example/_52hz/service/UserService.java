package com.example._52hz.service;

import com.example._52hz.util.APIResponse;

import javax.servlet.http.HttpSession;

public interface UserService {
    APIResponse getUserByStuNumber(String stuNumber);

    APIResponse setMyNickName(String nickname, HttpSession session);

    APIResponse getMyNickName(HttpSession session);
}
