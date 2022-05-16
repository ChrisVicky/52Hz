package com.example._52hz.dao;

import com.example._52hz.entity.FMsg;
import com.example._52hz.entity.Msg;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
*   -- >For Friendship Msg
* */
@Mapper
public interface FMsgMapper {
    @Select("select * from fMsg where fm_id = #{fm_id}")
    FMsg getMsgByFMsyId(@Param("fm_id") Integer fm_id);

    @Select("SELECT * FROM fMsg WHERE sender = #{sender}")
    List<FMsg> getMsgBySender(@Param("sender") Integer sender);

    @Select("SELECT * FROM fMsg WHERE r_id = #{r_id} ")
    List<FMsg> getMsgByR(@Param("r_id") Integer r_id);

    @Select("SELECT * FROM fMsg WHERE r_id = #{r_id} AND sender NOT IN (SELECT b_u_id FROM blackList WHERE u_id = #{r_id} AND blackList.is_deleted = 0);")
    List<FMsg> getMsgByRWithBL(@Param("r_id") Integer r_id);

    @Select("SELECT * FROM fMsg WHERE r_id = #{r_id} AND fMsg.is_read = 0 AND sender NOT IN (SELECT b_u_id FROM blackList WHERE u_id = #{r_id} AND blackList.is_deleted = 0);")
    List<FMsg> getMsgByRWithBLandReadFlag(@Param("r_id") Integer r_id);

    @Update("UPDATE fMsg SET r_id = #{r_id} WHERE r_wechat = #{r_wechat}")
    void updateRIdWithWechat(@Param("r_id") Integer r_id, @Param("r_wechat") String r_wechat);

    @Update("UPDATE fMsg SET r_id = #{r_id} WHERE r_Phone = #{r_Phone}")
    void updateRIdWithPhone(@Param("r_id") Integer r_id, @Param("r_Phone") String r_Phone);

    @Update("UPDATE fMsg SET r_id = #{r_id} WHERE r_stu_number = #{r_stu_number}")
    void updateRIdWithStuNumber(@Param("r_id") Integer r_id, @Param("r_stu_number") String r_stu_number);

    @Insert("INSERT INTO fMsg (sender, r_wechat, r_phone, r_stu_number, msg, created_at) " +
            " VALUES (#{sender}, #{r_wechat}, #{r_phone}, #{r_stu_number}, #{msg}, #{created_at})")
    void addOneMsgWithoutId(@Param("sender") Integer sender, @Param("r_wechat") String r_wechat, @Param("r_phone") String r_phone,
                   @Param("r_stu_number") String r_stu_number, @Param("msg") String msg,
                   @Param("created_at") String created_at);

    @Insert("INSERT INTO fMsg (sender, msg, r_id, created_at) VALUES " +
            "(#{sender},#{msg}, #{r_id}, #{created_at})")
    void addMsgWithId(@Param("sender") Integer sender, @Param("r_id") Integer r_id,
                      @Param("msg") String msg, @Param("created_at") String created_at);

    @Insert("INSERT INTO fMsg (sender, msg, r_id, r_stu_number, created_at) VALUES " +
            "(#{sender},#{msg}, #{r_id}, #{r_stu_number},#{created_at})")
    void addMsgWithIdStuNumber(@Param("sender") Integer sender, @Param("r_id") Integer r_id, @Param("r_stu_number") String r_stu_number,
                               @Param("msg") String msg, @Param("created_at") String created_at);

    @Insert("INSERT INTO fMsg (sender, msg, r_id, r_stu_number, r_wechat, r_phone, created_at) VALUES " +
            "(#{sender},#{msg}, #{r_id}, #{r_stu_number},#{r_wechat}, #{r_phone},#{created_at})")
    void addMsgWithAll(@Param("sender") Integer sender, @Param("r_id") Integer r_id,
                       @Param("r_stu_number") String r_stu_number, @Param("r_wechat") String r_wechat,
                               @Param("r_phone") String r_phone,
                               @Param("msg") String msg, @Param("created_at") String created_at);

    @Update("UPDATE fMsg SET is_read = 1 WHERE fm_id <= #{latest_id} AND r_id = #{u_id} AND sender = #{sender}")
    void setRead(@Param("latest_id") Integer latest_id, @Param("u_id") Integer u_id, @Param("sender") Integer sender);
}
