package com.example._52hz.dao;

import com.example._52hz.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
//@Component
public interface UserMapper {
    @Select("SELECT * FROM user WHERE u_id=#{u_id}")
    List<User> getUserByUId(@Param("u_id") String u_id);

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


    @Select("SELECT u_id FROM user WHERE phone=#{phone} and is_deleted=0;")
    List<Integer> getUIdByPhone(@Param("phone") String phone);

    @Select("SELECT u_id FROM user WHERE stu_number=#{stu_number} and is_deleted=0;")
    List<Integer> getUIdByStuNumber(@Param("stu_number") String stu_number);

    @Select("SELECT u_id FROM user WHERE email=#{email} and is_deleted=0;")
    List<Integer> getUIdByEmail(@Param("email") String email);

    @Select("SELECT u_id FROM user WHERE qq=#{qq} and is_deleted=0;")
    List<Integer> getUIdByQq(@Param("qq") String qq);

    @Select("SELECT u_id FROM user WHERE wechat=#{wechat} and is_deleted=0;")
    List<Integer> getUIdByWechat(@Param("wechat") String wechat);

    @Select("SELECT u_id FROM user WHERE u_name=#{u_name} and gender=#{gender} and grade=#{grade} and is_deleted=0;")
    List<Integer> getUIdByAmbiguous(@Param("u_name") String u_name, @Param("gender") String gender,
                                    @Param("grade") String grade);

    @Update("UPDATE user SET relationship_id = #{relationship_id} WHERE u_id = #{u_id} and is_deleted=0")
    void setRelationshipId(@Param("relationship_id") Integer relationshipId, @Param("u_id") Integer u_id);

    @Update("UPDATE user SET relationship_id = null WHERE relationship_id=#{r_id}")
    void deleteRelationshipId(@Param("r_id") Integer r_id);

    @Select("SELECT * FROM user where relationship_id = (SELECT relationship_id from user where u_id=#{u_id}) and u_id!=#{u_id};")
    List<User> getPartner(@Param("u_id") Integer u_id);
}
