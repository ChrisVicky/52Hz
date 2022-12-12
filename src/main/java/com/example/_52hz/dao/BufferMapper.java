package com.example._52hz.dao;


import com.example._52hz.entity.Buffer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.sql.Time;
import java.util.List;

@Mapper
public interface BufferMapper {
    //新建buffer
    @Insert("insert into buffer (u_id,stu_number,phone,qq,wechat,u_name,gender," +
            "grade,email,msg,updated_at,created_at) values (#{u_id},#{stu_number},#{phone}," +
            "#{qq},#{wechat},#{u_name},#{gender},#{grade},#{email},#{msg},#{updated_at},#{created_at})")
    int insertBuffer(@Param("u_id") Integer u_id,       @Param("stu_number") String stu_number,
                     @Param("phone") String phone,      @Param("qq") String qq,
                     @Param("wechat") String wechat,    @Param("u_name") String u_name,
                     @Param("gender") String gender,    @Param("grade") String grade,
                     @Param("email") String email,      @Param("msg") String msg,
                     @Param("updated_at")String updated_at, @Param("created_at") String created_at );

    //根据b_id更新msg和updated_at
    @Update("UPDATE buffer SET msg=#{msg},updated_at=#{updated_at} where b_id=#{b_id}")
    int updateBuffer(@Param("msg") String msg,@Param("updated_at") String update_at,@Param("b_id") Integer b_id);

    //根据b_id删除buffer
    @Update("UPDATE buffer SET is_deleted = 1, updated_at = #{updated_at} where b_id=#{b_id}")
    int deleteBuffer(@Param("b_id") int b_id, @Param("updated_at") String updated_at);


    //通过RelationShipId获取表白信息
    @Select("SELECT b_id,u_id,stu_number,phone,qq,wechat,u_name,gender,grade,email,msg,updated_at,created_at,b.is_deleted" +
            " FROM buffer b INNER JOIN relationship r  ON b.b_id = r.b_id_1 OR b.b_id = r.b_id_2 WHERE r.r_id = #{r_id} AND r.is_deleted=0 ;")
    List<Buffer> getBuffersByRid(@Param("r_id") Integer r_id);

    //通过u_id获取buffer
    @Select("SELECT * FROM buffer b WHERE b.u_id = #{u_id} AND b.is_deleted = 0;")
    List<Buffer> getBufferByUid(@Param("u_id") Integer u_id);

    @Select("SELECT * FROM buffer b WHERE b.u_id = #{u_id} AND b.is_matched = 0 AND b.is_deleted=0;")
    List<Buffer> getUnMatchedBufferByUid(@Param("u_id") Integer u_id);

    // Get Information
    @Select("SELECT * FROM buffer b WHERE b.u_id = #{u_id} AND b.is_deleted = 0;")
    List<Buffer> getUndeletedBufferByUid(@Param("u_id") Integer u_id);

    @Update("UPDATE buffer set is_matched=1, updated_at=#{updated_at} WHERE b_id=#{b_id}")
    int matchBuffer(@Param("b_id") Integer b_id, @Param("updated_at") String updated_at);

    @Select("SELECT is_matched FROM buffer WHERE b_id=#{b_id} AND is_deleted=0")
    Integer getIsMatchedByBId(@Param("b_id") Integer b_id);

    @Select("SELECT * FROM buffer WHERE b_id=#{b_id} AND is_deleted=0")
    Buffer getBufferByBId(@Param("b_id") Integer b_id);
}
