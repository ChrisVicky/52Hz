package com.example._52hz.dao;

import com.example._52hz.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    @Select("SELECT * FROM user WHERE stu_number = #{stu_number};")
    List<User> getUserByStuNumber(@Param("stu_number") String stu_number);

    // Newly Inserted User has no QQ AND Wechat because they were not Contained in the Original Database;
    @Insert("INSERT INTO user (`stu_number`, `u_name`, `grade`, `gender`, `phone`, `email`,  `created_at`, `updated_at`, `is_deleted`)" +
            "VALUES (#{stu_number}, #{u_name}, #{grade}, #{gender}, #{phone}, #{email},  #{created_at}, #{updated_at}, #{is_deleted})")
    void insertNewUser(@Param("stu_number") String stu_number,  @Param("u_name") String u_name,
                       @Param("grade") String grade,            @Param("gender") String gender,
                       @Param("phone") String phone,            @Param("email") String email,
                       @Param("created_at") String created_at,  @Param("updated_at") String updated_at,
                       @Param("major") String major,            @Param("department") String deparment,
                       @Param("campus") String campus,          @Param("is_deleted") Integer is_deleted  );

    @Update("UPDATE user SET major=#{major}, department=#{department}, campus=#{campus}, " +
            "updated_at=#{updated_at}, phone=#{phone}, grade=#{grade}, email=#{email} WHERE stu_number=#{stu_number};")
    void updateUserWhenLogin(
            @Param("major") String major,       @Param("department") String department,
            @Param("campus") String campus,     @Param("phone") String phone,
            @Param("email") String email,       @Param("grade") String grade,
            @Param("stu_number") String stu_number, @Param("updated_at") String updated_at);
}
