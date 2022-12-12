package com.example._52hz.util;/**
 * @Description TODO
 * @author christopher
 * @date 2022/3/29-上午12:34
 * @year 2022
 */

import org.springframework.context.annotation.Bean;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: _52Hz
 * @description: Get User's Ip
 * @author: 作者名字
 * @create: 2022-03-29 00:34
 */
public class IpHelper {
    @Bean
    public String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        System.out.println(ip);
        return ip;
    }
}
