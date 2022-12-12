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
import java.util.Objects;
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
    public APIResponse addMsg(String msg, String stu_number, String u_name, String phone, String wechat, String senderId,HttpSession session) {
        lock.lock();
        try{
            // convert Date
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            User me = (User) session.getAttribute("user");
            if(me==null){
                return APIResponse.error(ErrorCode.NOT_LOGIN_YET);
            }
            User target;
            List<User> userList;
            // Check Information, Could be Invalid
            if(stu_number.length()!=0){
//                System.out.println("stu_number: " + stu_number);
                userList = userMapper.getUserByStuNumber(stu_number);
            }else if(phone.length()!=0){
                userList = userMapper.getUserByPhone(phone);
            }else if(wechat.length()!=0){
                userList = userMapper.getUserByWechat(wechat);
            }else if(senderId!=null && senderId.length()!=0){
                userList = userMapper.getUserByUId(senderId);
            }else{
                return APIResponse.error(ErrorCode.PURSUIT_TARGET_NULL);
            }
            if(userList.isEmpty()){
                // The Target haven't joined our system yet.
                // This kind of error shall be done at his initial Login.
                fMsgMapper.addOneMsgWithoutId(me.getU_id(), wechat, phone, stu_number, msg, ft.format(date));
                return APIResponse.success("MSG SENT, BUT the Target has never logged in OUR 52Hz");
            }
            if(userList.contains(me)){
                return APIResponse.error(ErrorCode.LOVE_CONFESSION_RECEIVER_IS_YOURSELF);
            }
            // Information Error
            if(userList.size()>1){
                return APIResponse.error(ErrorCode.MULTIPLE_USER);
            }
            // Get it. Add as much information as possible
            target = userList.get(0);
            if(u_name.length()==0 || u_name.equals(target.getU_name()))
                fMsgMapper.addMsgWithAll(me.getU_id(), target.getU_id(), target.getStu_number(), target.getWechat(), target.getPhone(), msg, ft.format(date));
            else
                return APIResponse.error(ErrorCode.NAME_NOT_PAIRED);
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
            if(!Objects.equals(fMsg.getR_id(), user.getU_id())){
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
