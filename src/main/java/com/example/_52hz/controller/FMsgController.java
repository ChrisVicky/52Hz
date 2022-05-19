package com.example._52hz.controller;

import com.example._52hz.service.FConfService;
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
public class FMsgController {

    // FRIENDSHIP _ CONFESSION
    @Resource
    FConfService fConfService;


    // Friendship Confession (MORE LIKE A MSG TRANSFER)
    @PostMapping("/addFriendshipConfession")
    public APIResponse addFConfession(@RequestParam String msg,
                                      @RequestParam String stu_number,
                                      @RequestParam String u_name,
                                      @RequestParam String phone,
                                      @RequestParam String wechat,
                                      @RequestParam String sender_id,
                                      HttpSession httpSession){
        return fConfService.addMsg(msg, stu_number, u_name, phone, wechat, sender_id, httpSession);
    }

    @GetMapping("/getMySentFConfession")
    public APIResponse getMyFConfession(HttpSession session){
        return fConfService.getMyMsgSent(session);
    }

    @GetMapping("/getMyRecvFConfession")
    public APIResponse getMyRecvFConfession(HttpSession session){
        return fConfService.getMyMsgReceived(session);
    }

    // SET read-flag and GET new msg
    @PostMapping("/setReadFConfession")
    public APIResponse setReadFConfession(HttpSession session, Integer latestId){
        return fConfService.setReadFlag(session, latestId);
    }

    @GetMapping("/getNewFMsg")
    public APIResponse getNewFMsg(HttpSession session){
        return fConfService.getNewMsg(session);
    }
}
