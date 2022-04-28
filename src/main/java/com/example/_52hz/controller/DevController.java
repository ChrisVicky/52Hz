package com.example._52hz.controller;
/**
 * @Description TODO
 * @author christopher
 * @date 2022/3/29-上午12:23
 * @year 2022
 */

import com.example._52hz.entity.User;
import com.example._52hz.service.*;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.IpHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.PublicKey;

/**
 * @program: _52Hz
 * @description: For developer
 * @author: Christopher
 * @create: 2022-03-29 00:23
 */
@RestController
@RequestMapping("/dev")
public class DevController {
    @Resource
    HttpServletRequest httpServletRequest;

    @Resource
    LogService logService;

    @Resource
    ConfService confService;

    @Resource
    MsgService msgService;

    @GetMapping("/ip")
    public APIResponse getIpAddress(){

        IpHelper ipHelper = new IpHelper();
        String ip = ipHelper.getIpAddress(httpServletRequest);
        if(ip != null){
            return APIResponse.success(ip);
        }else{
            return APIResponse.error(ErrorCode.IP_ERROR);
        }
    }

    @PostMapping("/token/login")
    public APIResponse tokenLogin(@RequestParam("token") String token,
                                  HttpSession httpSession) throws IOException {
        return logService.tokenLogin(token, httpSession);
    }

    @PostMapping("/classic/login")
    public APIResponse login(@RequestParam("account") String account,
                             @RequestParam("password") String password,
                             HttpSession httpSession) throws IOException{
        return logService.classicLogin(account, password, httpSession);
    }

    @PostMapping("/back/door")
    public APIResponse logBack(@RequestParam("stu_number") String stu_number, HttpSession session){
        return logService.logBack(stu_number, session);
    }



}
