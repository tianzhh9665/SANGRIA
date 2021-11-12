package com.sangria.operation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sangria_game_info")
public class GameDO {
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String uuid;
	
	private String name;
	
	private String managerUuid;
	
	private String createTime;
	
	private String modifiedTime;

}

