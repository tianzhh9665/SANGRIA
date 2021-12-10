package com.sangria.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sangria_game_manager")
public class GameManagerDO {
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String uuid;
	
	private String username;
	
	private String password;
	
	private String token;
	
	private String createTime;
	
	private String modifiedTime;
	
	private String gameUuid;

}
