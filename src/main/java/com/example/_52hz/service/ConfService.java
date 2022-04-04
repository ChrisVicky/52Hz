package com.example._52hz.service;

import com.example._52hz.entity.User;
import com.example._52hz.util.APIResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Time;

@Service
public interface ConfService {

    /*
     * 添加告白
     * 添加成功返回"告白发送成功"
     * 添加失败返回ADD_CONFESSION_ERROR
     * */
    APIResponse addConfession(HttpSession session, String stu_number, String phone,
                              String qq, String wechat, String u_name, String gender,
                              String grade, String email, String msg);

    /*
     * 删除告白
     * 删除成功返回"删除告白成功"
     * 删除失败返回"DELETE_CONFESSION_ERROR"
     * */
    APIResponse deleteConfession(Integer b_id);

    /*
     * 更新告白
     * 更新成功返回"更新告白成功"
     * 更新失败返回"UPDATE_CONFESSION_ERROR"
     * */
    APIResponse updateConfession(String msg, HttpSession session);

    /**
     * 根据user进行buffer匹配
     */

    APIResponse matchConfessionByUserId(User user);

    APIResponse checkState (HttpSession session);
    APIResponse getMyConfession (HttpSession session);

}
