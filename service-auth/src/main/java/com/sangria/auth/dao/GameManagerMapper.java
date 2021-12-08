package com.sangria.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.sangria.auth.entity.GameManagerDO;

@Mapper
@Component
public interface GameManagerMapper extends BaseMapper<GameManagerDO> {

	// customized SQL update to set token value to null
	int updateLogout(@Param("token") String token);

}
