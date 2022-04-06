package com.example._52hz.dao;

import com.example._52hz.entity.Msg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.*;

import java.sql.Time;
import java.util.List;

@Mapper
public interface MsgMapper {

    @Select("select * from msg where m_id = #{m_id}")
    Msg getMsgByMsyId(@Param("m_id") Integer m_id);

    @Select("select * from msg where sender = #{sender} and is_deleted=0")
    List<Msg> getMsgListBySender(@Param("sender") Integer sender);

    @Select("select * from msg where receiver = #{receiver} and is_deleted=0")
    List<Msg> getMsgListByReceiver(@Param("receiver") Integer receiver);

    @Select("select * from msg where sender = #{sender} and receiver = #{receiver}")
    List<Msg> getMsgListBySenderAndReceiver(@Param("sender") Integer sender,@Param("receiver") Integer receiver);

    @Insert("insert into msg (`sender`,`receiver`,`msg`,`created_at`)" +
            "values (#{sender},#{receiver},#{msg},#{created_at})")
    int insertMsg(@Param("sender") Integer sender, @Param("receiver") Integer receiver,
                  @Param("msg")    String msg,     @Param("created_at") String createdAt);

    @Update("update msg set msg=#{msg} where m_id=#{m_id}")
    void updateMsg(@Param("m_id") Integer m_id,@Param("msg") String msg);

    @Update("update msg set is_deleted = 1 where m_id=#{m_id}")
    void deleteMsg(@Param("m_id")Integer m_id);


}
