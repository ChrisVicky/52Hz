package com.example._52hz.controller;

import com.example._52hz.service.MsgService;
import com.example._52hz.util.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @program: _52Hz
 * @description: Deal With Msg
 * @author: Christopher Liu
 * @create: 2022-04-07 23:49
 */

@RestController
@RequestMapping("/api")
public class MsgController {
    @Resource
    MsgService msgService;

    @GetMapping("/get/my/msg")
    public APIResponse myMsg(HttpSession httpSession){
        return msgService.getMyMsg(httpSession);
    }

    @PostMapping("/send/msg")
    public APIResponse sendMsg(@RequestParam("msg") String msg,
                               HttpSession httpSession){
        return msgService.sendMsg(msg, httpSession);
    }



}
