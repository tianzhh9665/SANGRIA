package com.sangria.operation.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangria.operation.entity.GameDO;

@Mapper
@Component
public interface GameMapper extends BaseMapper<GameDO>{

}
