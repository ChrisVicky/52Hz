package com.example._52hz.dao;

import com.example._52hz.entity.Xiaotang;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface XiaotangMapper {
    @Insert("INSERT INTO Xiaotang (u_id, Msg, created_at) VALUES (#{u_id}, #{msg}, #{created_at})")
    void addAConfession(@Param("u_id") Integer u_id, @Param("msg") String msg, @Param("created_at") String created_at);

    @Select("SELECT * FROM Xiaotang WHERE u_id = #{u_id} AND is_deleted = 0")
    List<Xiaotang> getAllConfessionByUNumber(@Param("u_id") Integer u_id);

    @Select("SELECT * FROM Xiaotang WHERE is_deleted=0")
    List<Xiaotang> getAllConfession();

    @Update("UPDATE Xiaotang SET is_deleted=1 WHERE id = #{id}")
    void deletedConfessionById(@Param("id") Integer id);

    @Select("SELECT * FROM Xiaotang WHERE Msg=#{msg}")
    List<Xiaotang>getXiaotangByMsg(@Param("msg") String msg);
}
