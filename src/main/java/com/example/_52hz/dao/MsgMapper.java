package com.example._52hz.dao;

import com.example._52hz.entity.Msg;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface MsgMapper extends Mapper<Msg> {
}
