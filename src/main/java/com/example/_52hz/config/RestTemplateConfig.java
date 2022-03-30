package com.example._52hz.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @program: _52Hz
 * @description: RestTemplate --> For login
 * @author: Christopher Liu
 * @create: 2022-03-29 20:34
 */
@Configuration
public class RestTemplateConfig {
    private static String domain = "52Hz.twt.edu.cn";
    private static String single_url = "https://api.twt.edu.cn/api/user/single";
    private static String post_url = "https://api.twt.edu.cn/api/auth/common";
    private static String ticket = "NTJIei5jNWFlZjI0MTEzYzk2Y2JiNGVmZmZkN2Y0YmNlY2M0MmNhZmExYmRi";
    public static String getDomain(){return domain;}
    public static String getTicket(){return ticket;}
    public static String getSingle_url(){return single_url;}
    public static String getPost_url(){return post_url; }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        RestTemplate restTemplate  = restTemplateBuilder.setConnectTimeout(Duration.ofMillis(5000L))
                .setReadTimeout(Duration.ofMillis(30000L)).build();
        return restTemplate;
    }
}
