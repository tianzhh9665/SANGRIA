package com.sangria.operation.controller;

import com.sangria.operation.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sangria.operation.service.InventoryService;

import io.swagger.annotations.Api;
/**
 * 
 * @author Steven Huang
 *
 */
@Api(tags = "Inventory Management Module")
@RestController
@RequestMapping("/inventory")
public class InventoryController extends BaseController{

	@Autowired
	InventoryService inventoryService;
	/**
	 * query inventory info
	 * @param token
	 * @param inventoryId
	 * @return
	 */
	@GetMapping(value = "/info")
	public ResponseDTO info(String token, String inventoryId) {
		if(StringUtils.isBlank(token)) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		
		return inventoryService.info(token, inventoryId);
	}
	/**
	 * add new inventory to the game
	 * @param dto
	 * @return
	 */
	@PostMapping(value = "/add")
	public ResponseDTO add(@RequestBody InventoryAddDTO dto) {
		String token = dto.getToken();
		if(StringUtils.isBlank(token)) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		
		return inventoryService.add(dto);
		
	}
	/**
	 * clear all items in a inventory
	 * @param dto
	 * @return
	 */
	@PostMapping(value = "/clear")
	public ResponseDTO clear(@RequestBody InventoryClearDTO dto) {
		String token = dto.getToken();
		String inventoryId = dto.getInventoryId();
		if(StringUtils.isBlank(token)) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		if(StringUtils.isBlank(inventoryId)) {
			return renderFail("ERROR: inventoryId can not be empty");
		}


		return inventoryService.clear(dto);
	}

}
