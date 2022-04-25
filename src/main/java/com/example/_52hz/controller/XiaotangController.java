package com.example._52hz.controller;

import com.example._52hz.service.XiaotangService;
import com.example._52hz.util.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @program: 52Hz
 * @description: Special For Xiaotang
 * @author: Christopher Liu
 * @create: 2022-04-08 12:54
 */
@RestController
@RequestMapping("/api/xiaotang")
public class XiaotangController {
    @Resource
    XiaotangService xiaotangService;

    @GetMapping("/getAll")
    APIResponse getAll(){
        return xiaotangService.getAllConfession();
    }

    @GetMapping("/getMy")
    APIResponse getMy(HttpSession session){
        return xiaotangService.getMyConfession(session);
    }

    @PostMapping("/deleteIt")
    APIResponse deleteIt(@RequestParam("id") Integer id) {
        return xiaotangService.deleteThisConfession(id);
    }

    @PostMapping("/addConfession")
    APIResponse addConfession(HttpSession session,
                                 @RequestParam("msg") String msg) throws IOException {
        return xiaotangService.addConfession(session, msg);
    }


}
