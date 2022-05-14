package com.example._52hz.controller;

import com.example._52hz.service.BLService;
import com.example._52hz.service.ConfService;
import com.example._52hz.service.UserService;
import com.example._52hz.util.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: _52Hz
 * @description: User Controller --> For user Issue
 * @author: Christopher Liu
 * @create: 2022-03-31 16:02
 */
@RestController
@RequestMapping("/api")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    ConfService confService;

    @Resource
    BLService blService;

    @GetMapping("/whoAmI")
    public APIResponse getWhoAmI(HttpSession httpSession){
        return userService.whoAmI(httpSession);
    }
    @GetMapping("/getUserByStuNumber")
    public APIResponse getUserByStuNumber(@RequestParam("stuNumber") String stuNumber){
        return userService.getUserByStuNumber(stuNumber);
    }

    // NICKNAME
    @PostMapping("/setMyNickname")
    public APIResponse setMyNickName(@RequestParam("nickname") String nickname,
                                     HttpSession session){
        return userService.setMyNickName(nickname, session);
    }

    @GetMapping("/getMyNickname")
    public APIResponse getMyNickname(HttpSession session){
        return userService.getMyNickName(session);
    }



    // BackList
    @PostMapping("/addBlackList")
    public APIResponse addBlackList(@RequestParam Integer b_u_id,
                                    HttpSession session){
        return blService.addBackList(b_u_id, session);
    }

    @PostMapping("/deleteBlackList")
    public APIResponse deleteBlackList(@RequestParam Integer b_u_id,
                                       HttpSession session){
        return blService.deletedFromBL(b_u_id, session);
    }

    @GetMapping("/getMyBlackList")
    public APIResponse getMyBlackList(HttpSession session){
        return blService.getMyBL(session);
    }

    @PostMapping("/getNickName")
    public APIResponse getNickName(@RequestParam Integer u_id){
        return userService.getNickName(u_id);
    }
}
