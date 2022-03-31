package com.example._52hz.service.impl;

import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.User;
import com.example._52hz.service.UserService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
