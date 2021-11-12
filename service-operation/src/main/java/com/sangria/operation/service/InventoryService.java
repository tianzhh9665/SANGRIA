package com.sangria.operation.service;

import com.sangria.operation.dto.InventoryAddDTO;
import com.sangria.operation.dto.InventoryClearDTO;
import com.sangria.operation.dto.ResponseDTO;

public interface InventoryService {
	
	ResponseDTO add(InventoryAddDTO dto);
	ResponseDTO clear(InventoryClearDTO dto);
	ResponseDTO info(String token, String inventoryId);

}
