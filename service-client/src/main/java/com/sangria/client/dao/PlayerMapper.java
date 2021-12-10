package com.sangria.client.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangria.client.entity.PlayerDO;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface PlayerMapper extends BaseMapper<PlayerDO> {


}
