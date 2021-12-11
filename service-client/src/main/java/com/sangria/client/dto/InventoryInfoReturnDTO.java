package com.sangria.client.dto;

import java.util.List;

import com.sangria.client.entity.ItemDO;

import lombok.Data;

@Data
public class InventoryInfoReturnDTO {
	
	String inventoryId;
	int itemCount;
	String createdAt;
	List<ItemDO> itemList;

}
