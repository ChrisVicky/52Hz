package com.example._52hz.dao;

import com.example._52hz.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
//@Component
public interface UserMapper {
    @Select("SELECT * FROM user WHERE stu_number = #{stu_number};")
    List<User> getUserByStuNumber(@Param("stu_number") String stu_number);

    // Newly Inserted User has no QQ AND Wechat because they were not Contained in the Original Database;
    @Insert("INSERT INTO user (`stu_number`, `u_name`, `grade`, `gender`, `phone`, `email`,  `created_at`, `updated_at`, `major`, `department`, `campus`, `is_deleted`)" +
            "VALUES (#{stu_number}, #{u_name}, #{grade}, #{gender}, #{phone}, #{email},  #{created_at}, #{updated_at}, #{major}, #{department}, #{campus},  #{is_deleted})")
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

    @Select("select * from user where  phone=#{phone}")
    List<User> getUserByPhone(@Param("phone") String phone);

    @Select("select * from user where qq=#{qq}")
    List<User> getUserByQq(@Param("qq") String qq);

    @Select("select * from user where wechat=#{wechat}")
    List<User> getUserByWechat(@Param("wechat") String wechat);

    @Select("select * from user where email=#{email}")
    List<User> getUserByEmail(@Param("email") String email);

    @Select("select * from user where u_name=#{u_name}")
    List<User> getUserByUName(@Param("u_name") String u_name);

    @Select("select * from user where u_name=#{u_name} and grade=#{grade}")
    List<User> getUserByUNameAndGrade(@Param("u_name") String u_name,@Param("grade") String grade);

    @Select("select * from user where u_name=#{u_name} and gender=#{gender}")
    List<User> getUserByUNameAndGender(@Param("u_name") String u_name,@Param("gender") String gender);

    @Select("select * from user where u_name=#{u_name} and gender=#{gender} and grade=#{grade}")
    List<User> getUserByUNameAndGenderAndGrade(@Param("u_name") String u_name,@Param("gender") String gender,String grade);

    //通过用户学生卡号获取relationshipID
    @Select("SELECT relationship_id FROM user WHERE user.stu_number = #{stu_number};")
    Integer getRelationIdByStuNumber(@Param("stu_number") String stu_number);
}
