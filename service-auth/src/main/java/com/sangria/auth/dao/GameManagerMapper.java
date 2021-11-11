package com.sangria.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangria.auth.entity.GameManagerDO;

@Mapper
@Component
public interface GameManagerMapper extends BaseMapper<GameManagerDO> {
	
	int getManagerNum();

}
