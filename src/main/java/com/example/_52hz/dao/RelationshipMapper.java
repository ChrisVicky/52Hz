package com.example._52hz.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface RelationshipMapper {
    @Insert("INSERT INTO relationship (`b_id_1`, `b_id_2`, `matched_at`, `is_deleted`) VALUES (#{b_id_1}, #{b_id_2}, #{matched_at}, 0);")
    void insertRelationship(@Param("b_id_1") Integer b_id_1, @Param("b_id_2") Integer b_id_2, @Param("matched_at") String matched_at);

    @Select("SELECT r_id FROM relationship WHERE b_id_1=#{b_id_1} and b_id_2 = #{b_id_2} and is_deleted=0;")
    Integer getRIdByBId1BId2(@Param("b_id_1") Integer b_id_1, @Param("b_id_2") Integer b_id_2);
}
