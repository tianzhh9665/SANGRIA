package com.sangria.operation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sangria.operation.dto.ItemAddDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.service.ItemService;
/**
 * 
 * @author Steven Huang
 *
 */
@RestController
@RequestMapping("/item")
public class ItemController extends BaseController{
	
	@Autowired
	ItemService itemService;
	/**
	 * add a item to a inventory
	 * @param dto
	 * @return
	 */
	@PostMapping(value = "/add")
	public ResponseDTO add(@RequestBody ItemAddDTO dto) {
		if(StringUtils.isBlank(dto.getToken())) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		if(StringUtils.isBlank(dto.getInventoryId())) {
			return renderFail("ERROR: inventoryId can not be empty");
		}
		if(StringUtils.isBlank(dto.getType())) {
			return renderFail("ERROR: type can not be empty");
		}
		
		if(dto.getName().length() > 64) {
			return renderFail("ERROR: name can not be longer than 64 characters");
		}
		
		if(dto.getType().length() > 64) {
			return renderFail("ERROR: name can not be longer than 64 characters");
		}
		
		if(StringUtils.isNotBlank(dto.getAttributes()) && dto.getAttributes().length() > 500) {
			return renderFail("ERROR: attributes can not be longer than 500 characters");
		}
		
		return itemService.add(dto);
	}
	/**
	 * query item info
	 * @param token
	 * @param itemId
	 * @return
	 */
	@GetMapping(value = "/info")
	public ResponseDTO info(String token, String itemId) {
		if(StringUtils.isBlank(token)) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		if(StringUtils.isBlank(itemId)) {
			return renderFail("ERROR: itemId can not be empty");
		}
		
		return itemService.info(token, itemId);
	}
	
	

}
