package com.example._52hz.service;

import com.example._52hz.util.APIResponse;

import javax.servlet.http.HttpSession;

public interface UserService {
    APIResponse whoAmI(HttpSession session);

    APIResponse getUserByStuNumber(String stuNumber);

    APIResponse setMyNickName(String nickname, HttpSession session);

    APIResponse getMyNickName(HttpSession session);

    APIResponse getNickName(Integer u_id);
}
