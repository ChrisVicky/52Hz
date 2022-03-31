package com.example._52hz.interceptor;

import com.alibaba.fastjson.JSON;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: _52Hz
 * @description: Interceptor --> For login Issue
 * @author: Christopher Liu
 * @create: 2022-03-31 15:31
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        if(request.getSession().getAttribute("user")!=null){
            return true;
        }
        APIResponse apiResponse = APIResponse.error(ErrorCode.NOT_LOGIN_YET);
        String content = JSON.toJSONString(apiResponse);
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(content);
        return false;
    }
}
