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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.util.List;
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

    @Override
    public APIResponse addConfession(Integer u_id, String stu_number, String phone,
                                     String qq, String wechat, String u_name, String gender,
                                     String grade, String email, String msg) {
        try{
            Buffer buffer = bufferMapper.getBufferByUid(u_id);
            if(buffer!=null) {
                return APIResponse.error(ErrorCode.ADD_CONFESSION_ERROR);
            }
            Time updated_at = new Time(System.currentTimeMillis());
            bufferMapper.insertBuffer(u_id, stu_number, phone, qq, wechat, u_name, gender, grade, email, msg, updated_at);
        } catch (Exception e) {
            e.printStackTrace();
            return APIResponse.error(ErrorCode.ADD_CONFESSION_ERROR);
        }
        return APIResponse.success("告白发送成功");
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
        Integer userId = user.getU_id();
        try {
            Buffer userBuffer = bufferMapper.getBufferByUid(userId);
            if (userBuffer == null) {
                return APIResponse.error(ErrorCode.NO_CONFESSION_ERROR);
            }
            Matcher matcher = new Matcher();
            List<User> userList = null;
            if(Matcher.isExactlyInfo(userBuffer)) {
                userList = matcher.exactlyMatchByBuffer(userBuffer);
                if(userList.size() == 0 || userList == null) {
                    //未匹配到对方，返回error，并提醒用户
                    return  APIResponse.error(ErrorCode.MATCHED_NULL_USER_ERROR);
                }
                if(userList.size() > 1) {
                    return APIResponse.error(ErrorCode.MATCHED_MULTIPLE_USERS_ERROR);
                }
                User secondUser=userList.get(0);
                Buffer secondUserBuffer = bufferMapper.getBufferByUid(secondUser.getU_id());
                if(Matcher.isExactlyInfo(secondUserBuffer) &&
                        Matcher.exactlyMatchBufferAndUser(secondUserBuffer,user)) {
                    //未完成
                }
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

