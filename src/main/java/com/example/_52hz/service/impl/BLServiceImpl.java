package com.example._52hz.service.impl;

import com.example._52hz.dao.BlackListMapper;
import com.example._52hz.entity.User;
import com.example._52hz.service.BLService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BLServiceImpl implements BLService {

    @Resource
    BlackListMapper blackListMapper;

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public APIResponse addBackList(Integer b_u_id, HttpSession session) {
        lock.lock();
        try{
            User user = (User) session.getAttribute("user");
            blackListMapper.addABlackList(user.getU_id(), b_u_id);
            return APIResponse.success("OK");
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse deletedFromBL(Integer b_u_id, HttpSession session) {
        lock.lock();
        try{
            User user = (User) session.getAttribute("user");
            blackListMapper.deleteABlackList(user.getU_id(), b_u_id);
            return APIResponse.success("OK");
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse getMyBL(HttpSession session) {
        try{
            User user = (User) session.getAttribute("user");
            return APIResponse.success(blackListMapper.getMyBlackList(user.getU_id()));
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }
}
