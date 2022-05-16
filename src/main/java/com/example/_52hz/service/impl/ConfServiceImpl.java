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
import com.example._52hz.util.TwtUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


    public List<User> getTargetUserByBuffer(Buffer buffer){
        if(buffer==null){
            return null;
        }
        List<User> userList;
        if(buffer.getStu_number().length()!=0){
            userList = userMapper.getUserByStuNumber(buffer.getStu_number());
        }else if(buffer.getPhone().length()!=0){
            userList = userMapper.getUserByPhone(buffer.getPhone());
        }else if(buffer.getQq().length()!=0){
            userList = userMapper.getUserByQq(buffer.getQq());
        }else if(buffer.getWechat().length()!=0){
            userList = userMapper.getUserByWechat(buffer.getWechat());
        }else if(buffer.getEmail().length()!=0){
            userList = userMapper.getUserByEmail(buffer.getEmail());
        }else{
            userList = userMapper.getUserByUNameAndGenderAndGrade(buffer.getU_name(),buffer.getGender(), buffer.getGrade());
        }
        return userList;
    }

    // Add Confession
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse addConfession(HttpSession session, String stu_number, String phone,
                                     String qq, String wechat, String u_name, String gender,
                                     String grade, String email, String msg) {
        try{

            User user = (User) session.getAttribute("user");
            if(user==null){
                return APIResponse.error(ErrorCode.USER_NOT_EXISTS);
            }
            Integer u_id = user.getU_id();
//            System.out.println(u_id);
            List<Buffer> buffer = bufferMapper.getBufferByUid(u_id);
            if(!buffer.isEmpty()) {
                // Had a Confession before
                return APIResponse.error(ErrorCode.HAD_CONFESSION_BEFORE_ERROR);
            }
//            System.out.println(u_id);
            String ret_string = "Add Confession Success.";
            String match_failed = " Match Failed";
            String match_success = " Match Success";

            // Now we begin with matching
            // First, Get List<Integer> uIdList of Pursuits
            List<Integer>uIdList;
            if(stu_number.length()!=0){
//                System.out.println(stu_number);
                uIdList = userMapper.getUIdByStuNumber(stu_number);
//                System.out.println(uIdList);
            }else if(phone.length()!=0){
                uIdList = userMapper.getUIdByPhone(phone);
            }else if(qq.length()!=0){
                uIdList = userMapper.getUIdByQq(qq);
            }else if(wechat.length()!=0){
                uIdList = userMapper.getUIdByWechat(wechat);
            }else if(email.length()!=0){
                uIdList = userMapper.getUIdByEmail(email);
            }else if(u_name.length()!=0 && gender.length()!=0 && grade.length()!=0){
                System.out.println(u_name + gender + grade);
                uIdList = userMapper.getUIdByAmbiguous(u_name, gender, grade);
            }else{
                return APIResponse.error(ErrorCode.PURSUIT_TARGET_NULL);
            }
//            System.out.println(u_id);
            // Check if the sender is myself
            if(!uIdList.isEmpty() && uIdList.contains(user.getU_id())){
                // Love msg may be sent to the sender.
                return APIResponse.error(ErrorCode.LOVE_CONFESSION_RECEIVER_IS_YOURSELF);
            }

            // Up to now, we made sure that parameters are Valid.
            // So add new confession to buffer
            // convert Date
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            System.out.println(ft.format(date));
            bufferMapper.insertBuffer(u_id, stu_number, phone, qq, wechat, u_name, gender, grade, email, msg, ft.format(date), ft.format(date));

            // Then we begin our match
            if(uIdList.isEmpty()){
                // Target have never ever logged in.
                // So No confession should he (she) make.
                return APIResponse.success(ret_string + match_failed);
            }
            // Target Valid, Maybe there are Confessions
            // 1. Get Buffer (Confessions) By UId
            // 2. Get new Target's Uid By Buffer
            // 3. Check is Match
            for(Integer uId : uIdList){
                List<Buffer> bufferList = bufferMapper.getUnMatchedBufferByUid(uId);
                if(bufferList.isEmpty()) {
                    continue;
                }
                List<User> userList = getTargetUserByBuffer(bufferList.get(0));
                if(userList!=null && userList.contains(user)){
                    // MATCH!!!
                    // 1. Set New Relationship
                    Integer bid1 = bufferList.get(0).getB_id();
                    Integer bid2 = bufferMapper.getUnMatchedBufferByUid(u_id).get(0).getB_id();
                    date = new Date();
                    ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    relationshipMapper.insertRelationship(bid1, bid2, ft.format(date));
                    // 2. Attach New Relationship to User
                    Integer r_id = relationshipMapper.getRIdByBId1BId2(bid1, bid2);
                    userMapper.setRelationshipId(r_id, u_id);
                    userMapper.setRelationshipId(r_id, uId);
                    // 3. Match Confession from Buffer to Prevent Multiple Match
                    bufferMapper.matchBuffer(bid1, ft.format(date));
                    bufferMapper.matchBuffer(bid2, ft.format(date));
                    return APIResponse.success(ret_string + match_success);
                }
            }
            return APIResponse.success(ret_string + match_failed);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.ADD_CONFESSION_ERROR);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse deleteConfession(HttpSession session) {
        lock.lock();
        try {

            User user = (User) session.getAttribute("user");
            if(user==null) {
                return APIResponse.error(ErrorCode.SESSION_ERROR);
            }
            List<Buffer> bufferList = bufferMapper.getUndeletedBufferByUid(user.getU_id());
            if(bufferList.isEmpty()){
                return APIResponse.error(ErrorCode.NO_CONFESSION_ERROR);
            }
            if(bufferList.size()>1){
                return APIResponse.error(ErrorCode.MULTIPLE_CONFESSIONS_ERROR);
            }
            Buffer buffer = bufferList.get(0);
            Integer b_id = buffer.getB_id();
            Integer is_matched = buffer.getIs_matched();
            if(is_matched == 1){
                // Matched, Delete Relationship as well
                List<Relationship> relationshipList = relationshipMapper.getRelationshipByBId(b_id);
                if(relationshipList.isEmpty()){
                    return APIResponse.error(ErrorCode.SERVICE_ERROR);
                }
                if(relationshipList.size()>1){
                    return APIResponse.error(ErrorCode.MULTIPLE_USER);
                }
                Relationship relationship = relationshipList.get(0);
                Integer anotherBId = relationship.getB_id_1() + relationship.getB_id_2() - b_id;
                // Delete Buffer
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                bufferMapper.deleteBuffer(b_id,ft.format(date));
                bufferMapper.deleteBuffer(anotherBId, ft.format(date));
                // Delete Relationship
                relationshipMapper.deleteRelationshipByRId(relationship.getR_id());
                // Delete User's Is_Match
                userMapper.deleteRelationshipId(relationship.getR_id());
            }else{
                // Not Matched, Delete Buffer only
                Date date = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                bufferMapper.deleteBuffer(b_id,ft.format(date));
            }
            return APIResponse.success("Confession deleted as well as the related Confession, Relationship and his is_matched status.");
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.DELETE_CONFESSION_ERROR);
        }finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse updateConfession(String msg, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            List<Buffer> bufferList = bufferMapper.getUndeletedBufferByUid(user.getU_id());
            if(bufferList.isEmpty()){
                return APIResponse.success("No Confession");
            }
            Integer b_id = bufferList.get(0).getB_id();
            Date date = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bufferMapper.updateBuffer(msg, ft.format(date), b_id);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.UPDATE_CONFESSION_ERROR);
        }
        return APIResponse.success("更新成功");
    }


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
            return APIResponse.success("匹配成功", bufferMapper.getBuffersByRid(relationShipId));
        }catch(Exception e){
            e.printStackTrace();
            return APIResponse.error(ErrorCode.SERVICE_ERROR);
        }finally{
            lock.unlock();
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public APIResponse getMyConfession (HttpSession session){
        lock.lock();
        try{
            User user  = (User) session.getAttribute("user");

            if(user==null){
                return APIResponse.error(ErrorCode.SESSION_ERROR);
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

