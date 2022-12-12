package com.example._52hz.dao;

import com.example._52hz.entity.Nickname;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NicknameMapper {

    @Insert("INSERT INTO nickname (u_id, nick_name) VALUES (#{u_id}, #{nick_name})")
    void addNewNickName(@Param("u_id") Integer u_id, @Param("nick_name") String nick_name);

    @Select("SELECT * FROM nickname WHERE u_id = #{u_id} and is_deleted = 0")
    List<Nickname> getMyNickName(@Param("u_id") Integer u_id);

    @Update("UPDATE nickname SET is_deleted = 1 WHERE u_id = #{u_id};")
    void deleteMyNickName(@Param("u_id") Integer u_id);

    @Update("UPDATE nickname SET nick_name = #{nick_name} WHERE u_id = #{u_id};")
    void updateMyNickName(@Param("u_id") Integer u_id, @Param("nick_name") String nick_name);

    @Select("SELECT * FROM nickname WHERE nick_name = #{nick_name} AND is_deleted = 0")
    List<Nickname> getCheckList(@Param("nick_name") String nick_name);
}

