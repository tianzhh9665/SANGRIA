package com.sangria.auth.dao;

import com.sangria.auth.entity.InventoryDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
@Component
public interface InventoryMapper extends BaseMapper<InventoryDO> {


}