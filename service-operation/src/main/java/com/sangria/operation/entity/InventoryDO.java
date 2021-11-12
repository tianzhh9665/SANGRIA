package com.sangria.operation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sangria_inventory_info")
public class InventoryDO {
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String uuid;
	
	private String gameUuid;
	
	private String createTime;
	
	private String modifiedTime;

}

