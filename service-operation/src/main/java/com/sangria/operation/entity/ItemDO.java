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
	
	private Integer type;
	
	private Integer price;
	
	private String ingredients;
	
	private String description;
	
	private String inventoryUuid;
	
	private String createTime;
	
	private String modifiedTime;

}


