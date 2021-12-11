package com.sangria.client.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangria.client.entity.InventoryDO;

@Mapper
@Component
public interface InventoryMapper extends BaseMapper<InventoryDO>{

}
