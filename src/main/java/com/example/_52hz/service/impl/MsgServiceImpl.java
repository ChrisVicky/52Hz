package com.example._52hz.service.impl;

import com.example._52hz.dao.MsgMapper;
import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.Msg;
import com.example._52hz.entity.User;
import com.example._52hz.service.MsgService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.Msgs;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: _52Hz
 * @description: Implement Msg Issues
 * @author: Christopher Liu
 * @create: 2022-04-06 11:40
 */
@Service
public class MsgServiceImpl implements MsgService {

    @Resource
    MsgMapper msgMapper;

    @Resource
    UserMapper userMapper;

    private ReentrantLock lock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse sendMsg(String msg, HttpSession httpSession) {
        try{
            lock.lock();
            User user = (User) httpSession.getAttribute("user");
            if(user==null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            List<User> userList = userMapper.getPartner(user.getU_id());
            if(userList.isEmpty()){
                return APIResponse.error(ErrorCode.NOT_MATCHED_YET_CANNOT_MSG);
            }
            if(userList.size()>1){
                return APIResponse.error(ErrorCode.MATCHED_MULTIPLE_USERS_ERROR);
            }
            int u_id = userList.get(0).getU_id();
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            msgMapper.insertMsg(user.getU_id(), u_id, msg, ft.format(date));
            return APIResponse.success("Msg Inserted.");
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR, e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse getMyMsg(HttpSession httpSession){
        try{
            User user = (User) httpSession.getAttribute("user");
            if(user==null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            List<Msg> msgSentList = msgMapper.getMsgListBySender(user.getU_id());
            List<Msg> msgReceiveList = msgMapper.getMsgListByReceiver(user.getU_id());
            return APIResponse.success(new Msgs(msgSentList, msgReceiveList));
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }
}
