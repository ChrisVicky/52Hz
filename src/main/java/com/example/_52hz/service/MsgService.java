package com.example._52hz.service;

import com.example._52hz.util.APIResponse;

import javax.servlet.http.HttpSession;

public interface MsgService {
    public APIResponse sendMsg(String msg, HttpSession httpSession);

    public APIResponse getMyMsg(HttpSession httpSession);
}
