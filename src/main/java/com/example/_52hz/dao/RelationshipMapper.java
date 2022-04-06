package com.example._52hz.dao;

import com.example._52hz.entity.Relationship;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface RelationshipMapper {
    @Insert("INSERT INTO relationship (`b_id_1`, `b_id_2`, `matched_at`, `is_deleted`) VALUES (#{b_id_1}, #{b_id_2}, #{matched_at}, 0);")
    void insertRelationship(@Param("b_id_1") Integer b_id_1, @Param("b_id_2") Integer b_id_2, @Param("matched_at") String matched_at);

    @Select("SELECT r_id FROM relationship WHERE b_id_1=#{b_id_1} and b_id_2 = #{b_id_2} and is_deleted=0;")
    Integer getRIdByBId1BId2(@Param("b_id_1") Integer b_id_1, @Param("b_id_2") Integer b_id_2);

    @Select("SELECT * FROM relationship WHERE b_id_1=#{b_id} or b_id_2=#{b_id} and is_deleted=0;")
    List<Relationship> getRelationshipByBId(@Param("b_id") Integer b_id);

    @Update("UPDATE relationship SET is_deleted=1 WHERE r_id=#{r_id}")
    void deleteRelationshipByRId(@Param("r_id") Integer r_id);


}
