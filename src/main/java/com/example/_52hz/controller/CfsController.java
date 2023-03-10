package com.example._52hz.controller;

import com.example._52hz.service.ConfService;
import com.example._52hz.util.APIResponse;
import org.apache.tomcat.jni.Time;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

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

    private ReentrantLock lock = new ReentrantLock();

    @PostMapping("/addConfession")
    public APIResponse addConfession(HttpSession session,    @RequestParam String stu_number,
                                     @RequestParam String phone,    @RequestParam String qq,
                                     @RequestParam String wechat,   @RequestParam String u_name,
                                     @RequestParam String gender,   @RequestParam String grade,
                                     @RequestParam String email,    @RequestParam String msg) {
        lock.lock();
        try{
//            Date date = new Date();
//            sleep(10);
//            System.out.println(date);
//            System.out.println("Test Lock" + stu_number);
//            date = new Date();
//            System.out.println(date);
            return confService.addConfession(session,stu_number,phone,qq,wechat,u_name,gender,grade,email,msg);
        }
        finally {
            lock.unlock();
        }
    }

    @PostMapping("/deleteConfession")
    public APIResponse deleteConfession(HttpSession session) {
        lock.lock();
        try{
            return confService.deleteConfession(session);
        }finally {
            lock.unlock();
        }
    }

    @PostMapping("/updateConfession")
    public  APIResponse updateConfession(@RequestParam("msg") String msg, HttpSession session) {
        return confService.updateConfession(msg, session);
    }

    //?????????????????????
    @GetMapping("/myConfession")
    public APIResponse myConfession (HttpSession session){
        return confService.getMyConfession(session);
    }


    //?????????????????????
    @GetMapping("/checkState")
    public APIResponse checkState (HttpSession session){
        return confService.checkState(session);
    }


}
