package com.example._52hz.service.impl;


import com.example._52hz.dao.XiaotangMapper;
import com.example._52hz.entity.User;
import com.example._52hz.entity.Xiaotang;
import com.example._52hz.service.XiaotangService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: 52Hz
 * @description: Xiaotang IMPLEMENT
 * @author: Christopher Liu
 * @create: 2022-04-08 12:58
 */
@Service
public class XiaotangServiceImpl implements XiaotangService {

    @Resource
    XiaotangMapper xiaotangMapper;

    ReentrantLock lock = new ReentrantLock();

    @Override
    public APIResponse getAllConfession() {
        try{
            return APIResponse.success(xiaotangMapper.getAllConfession());
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }

    /**
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse getMyConfession(HttpSession session) {
        try{
            User user = (User) session.getAttribute("user");
            return APIResponse.success(xiaotangMapper.getAllConfessionByUNumber(user.getU_id()));
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }
    }

    /**
     *
     * @param id    --> Confession Id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse deleteThisConfession(Integer id) {
        try{
            xiaotangMapper.deletedConfessionById(id);
            return APIResponse.success("Ok");
        }catch (Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }


    }

    /**
     *
     * @param msg   --> msg to be sent
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse addConfession(HttpSession session, String msg) throws IOException {
        try{
            lock.lock();
            User user = (User) session.getAttribute("user");
            List<Xiaotang> xiaotangList = xiaotangMapper.getXiaotangByMsg(msg);
            if(!xiaotangList.isEmpty()){
                for(Xiaotang xiaotang:xiaotangList){
//                    System.out.println(xiaotang.getId() + " SDf " + user.getId());
                    if(Objects.equals(xiaotang.getU_id(), user.getU_id())){
                        return APIResponse.error(ErrorCode.SERVICE_ERROR, "Same Msg From Same User!");
                    }
                }
            }

//            System.out.println(user);
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            xiaotangMapper.addAConfession(user.getU_id(), msg, ft.format(date));
            return APIResponse.success("Ok");
        }catch (Exception e){
            return APIResponse.error(ErrorCode.SERVICE_ERROR, e.getMessage());
        }finally {
            lock.unlock();
        }
    }
}
