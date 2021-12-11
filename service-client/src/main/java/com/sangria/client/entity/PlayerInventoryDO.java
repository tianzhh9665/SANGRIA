package com.sangria.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("sangria_player_inventory_info")
public class PlayerInventoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String uuid;

    private String playerUuid;

    private String itemUuid;

    private Integer amount;

    private String createTime;

    private String modifiedTime;

}