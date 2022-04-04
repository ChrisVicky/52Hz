package com.example._52hz.controller;
/**
 * @Description TODO
 * @author christopher
 * @date 2022/3/29-上午12:23
 * @year 2022
 */

import com.example._52hz.entity.User;
import com.example._52hz.service.ConfService;
import com.example._52hz.service.LogService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.IpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @program: _52Hz
 * @description: For developer
 * @author: 作者名字
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



    @PostMapping("/deleteConfession")
    public APIResponse deleteConfession(@RequestParam("b_id") Integer b_id) {
        return confService.deleteConfession(b_id);
    }




    @PostMapping("/back/door")
    public APIResponse logBack(@RequestParam("stu_number") String stu_number, HttpSession session){
        return logService.logBack(stu_number, session);
    }
}
