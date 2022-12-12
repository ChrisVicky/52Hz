package com.example._52hz.service;

import com.example._52hz.util.APIResponse;

import javax.servlet.http.HttpSession;

public interface BLService {
    APIResponse addBackList(Integer b_u_id, HttpSession session);
    APIResponse deletedFromBL(Integer b_u_id, HttpSession session);
    APIResponse getMyBL(HttpSession session);
}
