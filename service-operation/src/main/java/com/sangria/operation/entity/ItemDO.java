package com.sangria.operation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sangria_item_info")
public class ItemDO {
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String uuid;
	
	private String name;
	
	private String type;
	
	private String inventoryUuid;
	
	private String createTime;
	
	private String modifiedTime;
	
	private String attributes;

}


