package com.example._52hz.dao;

import com.example._52hz.entity.BlackList;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlackListMapper {
    @Insert("INSERT INTO blackList (u_id, b_u_id, is_deleted) VALUES (#{u_id}, #{b_u_id}, 0)")
    void addABlackList(@Param("u_id") Integer u_id, @Param("b_u_id") Integer b_u_id);

    @Update("UPDATE blackList SET is_deleted = 1 WHERE u_id = #{u_id} AND b_u_id = #{b_u_id}")
    void deleteABlackList(@Param("u_id") Integer u_id, @Param("b_u_id") Integer b_u_id);

    @Select("SELECT * FROM blackList WHERE u_id = #{u_id} AND is_deleted = 0")
    List<BlackList> getMyBlackList(@Param("u_id") Integer u_id);

    @Select("SELECT * FROM blackList WHERE b_u_id = #{b_u_id}")
    List<BlackList> getWhoBannedMe(@Param("b_u_id") Integer b_u_id);
}
