package com.example._52hz.util;

import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.Buffer;
import com.example._52hz.entity.User;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

public class Matcher {
    @Resource
    UserMapper userMapper;

    /*
     * 是否是精确信息
     * 前端要求精确信息只填一个
     * 前端要求模糊信息必须要填名字
     *
     * Christopher : NULL!!!! Precision should not be restricted.
     */

    public static boolean isExactlyInfo(Buffer buffer) {
        return ((buffer.getStu_number()!=null) || (buffer.getEmail()!=null) ||
                (buffer.getQq()!=null) || (buffer.getWechat()!=null) ||
                (buffer.getPhone()!=null));
    }

    /*
     * 根据buffer进行准确查找
     */

    public static boolean exactlyMatchBufferAndUser(Buffer buffer,User user) {

        if(buffer.getStu_number()!=null && buffer.getStu_number().equals(user.getStu_number())) {
            return true;
        }
        if(buffer.getPhone()!=null && buffer.getPhone().equals(user.getPhone())) {
            return true;
        }
        if(buffer.getQq()!=null && buffer.getQq().equals(user.getQq())) {
            return true;
        }
        if(buffer.getWechat()!=null && buffer.getWechat().equals(user.getWechat())) {
            return true;
        }
        if(buffer.getEmail()!=null && buffer.getEmail().equals(user.getEmail())) {
            return true;
        }

        return false;

    }

    public List<User> exactlyMatchByBuffer(Buffer buffer) {
        if(buffer == null) {
            return null;
        }
        List<User> userList = null;
        try{
            if(buffer.getStu_number() != null) {
                userList = userMapper.getUserByStuNumber(buffer.getStu_number());
            }

            if(buffer.getPhone() != null) {
                userList = userMapper.getUserByPhone(buffer.getPhone());
            }

            if(buffer.getQq() != null) {
                userList = userMapper.getUserByQq(buffer.getQq());
            }

            if(buffer.getWechat() != null) {
                userList = userMapper.getUserByWechat(buffer.getWechat());
            }

            if(buffer.getEmail() != null) {
                userList = userMapper.getUserByEmail(buffer.getEmail());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    /*
     * 根据buffer进行模糊查找
     */
    public List<User> fuzzyMatch(Buffer buffer) {
        if(buffer == null) {
            return null;
        }
        List<User> userList = new ArrayList<>();
        String userName = buffer.getU_name();
        String gender = buffer.getGender();
        String grade = buffer.getGrade();
        if(userName != null) {
            if(gender != null) {
                if(grade != null) {
                    userList = userMapper.getUserByUNameAndGenderAndGrade(userName,gender,grade);
                }
                else {
                    userList = userMapper.getUserByUNameAndGender(userName,gender);
                }
            }
            else if(grade != null) {
                userList = userMapper.getUserByUNameAndGrade(userName,grade);
            }    else {
                userList = userMapper.getUserByUName(userName);
            }
        }
        return userList;
    }


}
