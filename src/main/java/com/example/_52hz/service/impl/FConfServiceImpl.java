package com.example._52hz.service.impl;

import com.example._52hz.dao.FMsgMapper;
import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.FMsg;
import com.example._52hz.entity.User;
import com.example._52hz.service.FConfService;
import com.example._52hz.service.UserService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class FConfServiceImpl implements FConfService {

    @Resource
    FMsgMapper fMsgMapper;

    @Resource
    UserMapper userMapper;

    private ReentrantLock lock = new ReentrantLock();


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse addMsg(String msg, String stu_number, String phone, String wechat, HttpSession session) {
        lock.lock();
        try{
            // convert Date
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            User me = (User) session.getAttribute("user");
            if(me==null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            User target;
            List<User> userList;
            // Check Information, Could be Invalid
            if(!stu_number.equals("")){
                userList = userMapper.getUserByStuNumber(stu_number);
            }else if(!phone.equals("")){
                userList = userMapper.getUserByPhone(phone);
            }else{
                userList = userMapper.getUserByWechat(wechat);
            }
            if(userList.isEmpty()){
                // The Target haven't joined our system yet.
                // This kind of error shall be done at his initial Login.
                fMsgMapper.addOneMsgWithoutId(me.getU_id(), wechat, phone, stu_number, msg, ft.format(date));
                return APIResponse.success("MSG SENT, BUT THIS Target has never logged in OUR 52Hz");
            }
            // Information Error
            if(userList.size()>1){
                return APIResponse.error(ErrorCode.MULTIPLE_USER);
            }
            // Get it.
            target = userList.get(0);
            fMsgMapper.addMsgWithId(me.getU_id(), target.getU_id(), msg, ft.format(date));
            return APIResponse.success("MSG SENT");
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse getMyMsgSent(HttpSession session) {
        try {
            User me = (User) session.getAttribute("user");
            if(me == null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            return APIResponse.success(fMsgMapper.getMsgBySender(me.getU_id()));
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }

    @Override
    public APIResponse getMyMsgReceived(HttpSession session) {
        try {
            User me = (User) session.getAttribute("user");
            if(me == null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            return APIResponse.success(fMsgMapper.getMsgByRWithBL(me.getU_id()));
        }catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse setReadFlag(HttpSession session, Integer latestId) {
        lock.lock();
        try{
            User user = (User) session.getAttribute("user");
            if(user==null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            FMsg fMsg = fMsgMapper.getMsgByFMsyId(latestId);
            if(fMsg == null){
                return APIResponse.error(ErrorCode.FMSG_ID_NOT_ERROR);
            }
            if(fMsg.getR_id() != user.getU_id()){
                return APIResponse.error(ErrorCode.NOT_YOUR_MSG);
            }
            fMsgMapper.setRead(latestId, user.getU_id(), fMsg.getSender());
            return APIResponse.success("OK");
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse getNewMsg(HttpSession session) {
        try {
            User me = (User) session.getAttribute("user");
            if(me == null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            return APIResponse.success(fMsgMapper.getMsgByRWithBLandReadFlag(me.getU_id()));
        }catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }




}
