package com.sangria.operation.service;

import com.sangria.operation.dto.ItemAddDTO;
import com.sangria.operation.dto.ResponseDTO;

public interface ItemService {
	
	ResponseDTO add(ItemAddDTO dto);
	ResponseDTO info(String token, String itemUuid);

}
