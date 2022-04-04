package com.example._52hz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example._52hz.config.RestTemplateConfig;
import com.example._52hz.dao.BufferMapper;
import com.example._52hz.dao.RelationshipMapper;
import com.example._52hz.dao.UserMapper;
import com.example._52hz.entity.Buffer;
import com.example._52hz.entity.Relationship;
import com.example._52hz.entity.User;
import com.example._52hz.service.ConfService;
import com.example._52hz.util.APIResponse;
import com.example._52hz.util.ErrorCode;
import com.example._52hz.util.Matcher;
import com.example._52hz.util.TwtUser;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ConfServiceImpl implements ConfService {

    @Resource
    UserMapper userMapper;

    @Resource
    BufferMapper bufferMapper;

    @Resource
    RelationshipMapper relationshipMapper;

    @Resource
    Matcher matcher;


    private ReentrantLock lock = new ReentrantLock();

    private TwtUser httpGetUserWithToken(String token) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            // Set Header,
            // Required Method --> GET
            Request request =
                    new Request.Builder()
                            .url(RestTemplateConfig.getSingle_url())
                            .addHeader("domain", RestTemplateConfig.getDomain())
                            .addHeader("ticket", RestTemplateConfig.getTicket())
                            .addHeader("token", token)
                            .build();
            Response response = client.newCall(request).execute();
            if(response.body()==null){
                return null;
            }
            String body = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(body).getJSONObject("result");
            response.close();
            // String --> to Json  --> to TwtUser
            return JSON.toJavaObject(jsonObject, TwtUser.class);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 1. Add Confession
     * 2. Check is_match
     * @param session
     * @param stu_number
     * @param phone
     * @param qq
     * @param wechat
     * @param u_name
     * @param gender
     * @param grade
     * @param email
     * @param msg
     * @return
     */
    @Override
    public APIResponse addConfession(HttpSession session, String stu_number, String phone,
                                     String qq, String wechat, String u_name, String gender,
                                     String grade, String email, String msg) {
        try{
            User user = (User) session.getAttribute("user");
            if(user==null){
                return APIResponse.error(ErrorCode.USER_NOT_EXISTS);
            }
            Integer u_id = user.getU_id();
            List<Buffer> buffer = bufferMapper.getBufferByUid(u_id);
            if(!buffer.isEmpty()) {
                // Had a Confession before
                return APIResponse.error(ErrorCode.ADD_CONFESSION_ERROR);
            }
            String ret_string = "Add Confession Success.";
            String match_failed = " Match Failed";
            String match_success = " Match Success";
            // Now we begin with matching
            // First, Get List<Integer> uIdList of Pursuits
            List<Integer>uIdList = new ArrayList<>();
            if(stu_number.length()!=0){
                uIdList = userMapper.getUIdByStuNumber(stu_number);
            }else if(phone.length()!=0){
                uIdList = userMapper.getUIdByPhone(phone);
            }else if(qq.length()!=0){
                uIdList = userMapper.getUIdByQq(qq);
            }else if(wechat.length()!=0){
                uIdList = userMapper.getUIdByWechat(wechat);
            }else if(email.length()!=0){
                uIdList = userMapper.getUIdByEmail(email);
            }else if(u_name.length()!=0 && gender.length()!=0 && grade.length()!=0){
                uIdList = userMapper.getUIdByAmbiguous(u_name, gender, grade);
            }else{
                return APIResponse.error(ErrorCode.PURSUIT_TARGET_NULL);
            }
            // Up to now, we made sure that parameters are Valid.
            // So add new confession to buffer
            Time updated_at = new Time(System.currentTimeMillis());
            bufferMapper.insertBuffer(u_id, stu_number, phone, qq, wechat, u_name, gender, grade, email, msg, updated_at);
            // Then we begin our match
            if(uIdList.isEmpty()){
                return APIResponse.success(ret_string + match_failed);
            }
            // Get Buffer By UId
            // Get Target By Buffer
            // Check if Match
            for(Integer uId : uIdList){
                List<Buffer> bufferList = bufferMapper.getBufferByUid(uId);
                if(bufferList.isEmpty()) {
                    continue;
                }
                List<User> userList = matcher.getUserByBuffer(bufferList.get(0));
                for(User _newUser : userList){
                    if(Objects.equals(_newUser.getU_id(), u_id)){
                        // MATCH!!!
                        // 1. Set New Relationship
                        Integer bid1 = bufferList.get(0).getB_id();
                        Integer bid2 = bufferMapper.getBufferByUid(u_id).get(0).getB_id();
                        Time matched_at = new Time(System.currentTimeMillis());
                        relationshipMapper.insertRelationship(bid1, bid2, matched_at.toString());
                        // 2. Attach New Relationship to User
                        Integer r_id = relationshipMapper.getRIdByBId1BId2(bid1, bid2);
                        userMapper.setRelationshipId(r_id, u_id);
                        userMapper.setRelationshipId(r_id, uId);
                        // 3. Delete Confession from Buffer to Prevent Multiple Match
                        deleteConfession(bid1);
                        deleteConfession(bid2);
                        return APIResponse.success(ret_string + match_success);
                    }
                }
            }
            return APIResponse.success(ret_string + match_failed);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.ADD_CONFESSION_ERROR);
        }
    }

    @Override
    public APIResponse deleteConfession(Integer b_id) {
        try {
            Time updated_at = new Time(System.currentTimeMillis());
            bufferMapper.deleteBuffer(b_id,updated_at);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.DELETE_CONFESSION_ERROR);
        }
        return  APIResponse.success("删除成功");
    }

    @Override
    public APIResponse updateConfession(String msg, Integer b_id) {
        try {
            Time updated_at = new Time(System.currentTimeMillis());
            bufferMapper.updateBuffer(msg, updated_at, b_id);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.UPDATE_CONFESSION_ERROR);
        }
        return APIResponse.success("更新成功");
    }

    @Override
    public APIResponse matchConfessionByUserId(User user) {
        if(user.getRelationship_id() != 0) {
            return APIResponse.success("已经匹配成功");
        }
        return APIResponse.success("Not Matched Yet");
    }

    /**
     *查看表白状态
     *      返回isMatch状态
     *      两条表白信息
     * @param session
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse checkState (HttpSession session){
        lock.lock();
        try{
            User user  = (User) session.getAttribute("user");

            if(user==null){
                return APIResponse.error(ErrorCode.TOKEN_LOGIN_ERROR);
            }

            String stuNumber = user.getStu_number();
            Integer relationShipId = userMapper.getRelationIdByStuNumber(stuNumber);

            //判断是否匹配成功
            //匹配失败则返回匹配失败
            if(relationShipId == null){
                return APIResponse.success("匹配失败");
            }
            //匹配成功获取两条消息
            return new APIResponse("匹配成功",bufferMapper.getBuffersByRid(relationShipId));
        }catch(Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally{
            lock.unlock();
        }

    }

    /**
     * 获取自己的表白
     * @param session
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse getMyConfession (HttpSession session){
        lock.lock();
        try{
            User user  = (User) session.getAttribute("user");

            if(user==null){
                return APIResponse.error(ErrorCode.TOKEN_LOGIN_ERROR);
            }

            Integer uid = user.getU_id();
            return APIResponse.success(bufferMapper.getBufferByUid(uid));
        }catch(Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally{
            lock.unlock();
        }

    }
}

