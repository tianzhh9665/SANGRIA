package com.sangria.operation.dto;

import java.util.List;

import com.sangria.operation.entity.ItemDO;

import lombok.Data;

@Data
public class InventoryInfoReturnDTO {
	
	String inventoryId;
	int itemCount;
	String createdAt;
	List<ItemDO> itemList;

}
