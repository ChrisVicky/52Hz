package com.example._52hz.service.impl;

import com.example._52hz.dao.NicknameMapper;
import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.Nickname;
import com.example._52hz.entity.User;
import com.example._52hz.service.UserService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @program: _52Hz
 * @description: User Service
 * @author: Christopher Liu
 * @create: 2022-03-31 16:08
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;

    @Resource
    NicknameMapper nicknameMapper;

    @Override
    public APIResponse whoAmI(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user==null){
            return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
        }
        return APIResponse.success(user);
    }

    @Override
    public APIResponse getUserByStuNumber(String stuNumber){
        try{
            List<User>userList = userMapper.getUserByStuNumber(stuNumber);
            if(userList.isEmpty()){
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
            if(userList.size()>1){
                return APIResponse.error(ErrorCode.MULTIPLE_USER);
            }
            return APIResponse.success(userList.get(0));
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    @Override
    public APIResponse setMyNickName(String nickname, HttpSession session) {
        try{
            User user = (User) session.getAttribute("user");
            if(user==null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }

            List<Nickname> checkList = nicknameMapper.getCheckList(nickname);
            if(!checkList.isEmpty()){
                return APIResponse.error(ErrorCode.NICKNAME_TAKEN);
            }

            List<Nickname> nicknameList = nicknameMapper.getMyNickName(user.getU_id());
            if(nicknameList.isEmpty()){
                nicknameMapper.addNewNickName(user.getU_id(), nickname);
            }else{
                nicknameMapper.updateMyNickName(user.getU_id(), nickname);
            }
            return APIResponse.success("Nickname Set");
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }

    @Override
    public APIResponse getMyNickName(HttpSession session){
        try{
            User user = (User) session.getAttribute("user");
            if(user==null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            List<Nickname> nicknameList = nicknameMapper.getMyNickName(user.getU_id());
            if(nicknameList.isEmpty()){
                return APIResponse.error(ErrorCode.NO_NICK_NAME_YET);
            }
            return APIResponse.success(nicknameList.get(0));
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }

    @Override
    public APIResponse getNickName(Integer u_id){
        try{
            List<Nickname> nicknameList = nicknameMapper.getMyNickName(u_id);
            if(nicknameList.isEmpty()){
                return APIResponse.error(ErrorCode.NO_NICK_NAME_YET);
            }
            if(nicknameList.size() > 1){
                return APIResponse.error(ErrorCode.MULTIPLE_NICK_NAME);
            }
            return APIResponse.success(nicknameList.get(0));
        }catch (Exception e){
            e.printStackTrace();
                   return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }
}
