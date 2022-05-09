package com.example._52hz.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: _52Hz
 * @description: Add Response Header Filter
 * @author: Christopher Liu
 * @create: 2022-05-08 15:10
 */
@Component
public class ResponseHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, HEAD");
        response.setHeader("Access-Control-Allow-Headers", "Origin");
        response.setHeader("Access-Control-Allow-Origin", "localhost:7070");
        filterChain.doFilter(request,response);
    }
}
