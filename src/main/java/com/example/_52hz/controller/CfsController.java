package com.example._52hz.controller;

import com.example._52hz.service.ConfService;
import com.example._52hz.util.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @program: _52Hz
 * @description: Controller about Confessions
 * @author: Christopher Liu
 * @create: 2022-04-04 23:02
 */
@RestController
@RequestMapping("/api")
public class CfsController {
    @Resource
    ConfService confService;


    @PostMapping("/addConfession")
    public APIResponse addConfession(HttpSession session,    @RequestParam String stu_number,
                                     @RequestParam String phone,    @RequestParam String qq,
                                     @RequestParam String wechat,   @RequestParam String u_name,
                                     @RequestParam String gender,   @RequestParam String grade,
                                     @RequestParam String email,    @RequestParam String msg) {
        return confService.addConfession(session,stu_number,phone,qq,wechat,u_name,gender,grade,email,msg);
    }

    @PostMapping("/deleteConfession")
    public APIResponse deleteConfession(HttpSession session) {
        return confService.deleteConfession(session);
    }

    @PostMapping("/updateConfession")
    public  APIResponse updateConfession(@RequestParam("msg") String msg, HttpSession session) {
        return confService.updateConfession(msg, session);
    }

    //获取自己的表白
    @GetMapping("/myConfession")
    public APIResponse myConfession (HttpSession session){
        return confService.getMyConfession(session);
    }


    //查看对方的告白
    @GetMapping("/checkState")
    public APIResponse checkState (HttpSession session){
        return confService.checkState(session);
    }


}
