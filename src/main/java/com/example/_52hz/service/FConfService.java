package com.example._52hz.service;

import com.example._52hz.util.APIResponse;

import javax.servlet.http.HttpSession;

public interface FConfService {
    // Add New Msg
    APIResponse addMsg(String msg, String stu_number, String phone, String wechat, HttpSession session);
    APIResponse getMyMsgSent(HttpSession session);
    APIResponse getMyMsgReceived(HttpSession session);
    APIResponse setReadFlag(HttpSession session, Integer latestId);
    APIResponse getNewMsg(HttpSession session);
}

