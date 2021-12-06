package com.sangria.operation.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.sangria.operation.dto.ItemAddDTO;
import com.sangria.operation.dto.ItemMakeDTO;
import com.sangria.operation.dto.ResponseDTO;

public interface ItemService {
	
	ResponseDTO add(ItemAddDTO dto);
	ResponseDTO info(String token, String itemUuid);
	ResponseDTO make(ItemMakeDTO dto);

}
