package com.example._52hz.controller;
/**
 * @Description TODO
 * @author christopher
 * @date 2022/3/29-上午12:23
 * @year 2022
 */

import com.example._52hz.service.LogService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.IpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: _52Hz
 * @description: For developer
 * @author: 作者名字
 * @create: 2022-03-29 00:23
 */
@RestController
public class DevController {
    @Resource
    HttpServletRequest httpServletRequest;

    @Resource
    LogService logService;

    @GetMapping("/dev/ip")
    public APIResponse getIpAddress(){

        IpHelper ipHelper = new IpHelper();
        String ip = ipHelper.getIpAddress(httpServletRequest);
        if(ip != null){
            return APIResponse.success(ip);
        }else{
            return APIResponse.error(ErrorCode.IP_ERROR);
        }
    }

    @PostMapping("/dev/token/login")
    public APIResponse tokenLogin(@RequestParam("token") String token) throws IOException {
        return logService.tokenLogin(token);
    }
}
