package com.sangria.operation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sangria.operation.Enum.ItemTypeEnum;
import com.sangria.operation.dto.ItemAddDTO;
import com.sangria.operation.dto.ItemMakeDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.service.ItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 
 * @author Steven Huang
 *
 */
@Api(tags = "Item Management Module")
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
	@ApiOperation(value = "Add a new item to specified inventory")
	@PostMapping(value = "/add")
	public ResponseDTO add(@RequestBody ItemAddDTO dto) {
		if(StringUtils.isBlank(dto.getToken())) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		if(StringUtils.isBlank(dto.getInventoryId())) {
			return renderFail("ERROR: inventoryId can not be empty");
		}
		if(dto.getType() == null) {
			return renderFail("ERROR: type can not be empty");
		}
		if(dto.getName().length() > 64) {
			return renderFail("ERROR: name can not be longer than 64 characters");
		}
		if(dto.getType() == ItemTypeEnum.COM_AND_TRA.getType()) {
			if(dto.getIngredients() == null || dto.getIngredients().size() == 0) {
				return renderFail("ERROR: you need to specify Ingredients for compostable item");
			}
			if(dto.getIngredients().size() > 3) {
				return renderFail("ERROR: one item can only have up to 3 ingredients");
			}
			if(dto.getPrice() == null) {
				return renderFail("ERROR: you need to specify price for tradable item");
			}
		}else if(dto.getType() == ItemTypeEnum.COM_AND_NOT_TRA.getType()) {
			if(dto.getIngredients() == null || dto.getIngredients().size() == 0) {
				return renderFail("ERROR: you need to specify Ingredients for compostable item");
			}
			if(dto.getIngredients().size() > 3) {
				return renderFail("ERROR: one item can only have up to 3 ingredients");
			}
		}else if(dto.getType() == ItemTypeEnum.NOT_COM_AND_TRA.getType()) {
			if(dto.getPrice() == null) {
				return renderFail("ERROR: you need to specify price for tradable item");
			}
		}else if(StringUtils.isNotBlank(dto.getDescription()) && dto.getDescription().length() > 64) {
			return renderFail("ERROR: description should be 64 characters most");
		}else {
			return renderFail("ERROR: invalid item type");
		}
		
		return itemService.add(dto);
	}

	/**
	 * query item info
	 * @param token
	 * @param itemId
	 * @return
	 */
	@ApiOperation(value = "query an item's info")
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
	
	/**
	 * make an item according to its ingredient list
	 */
	@ApiOperation(value = "make an item according to its ingredient list")
	@PostMapping(value = "/make")
	public ResponseDTO make(@RequestBody ItemMakeDTO dto) {
		if(StringUtils.isBlank(dto.getToken())) {
			return renderFail("ERROR: authentication failed, token can not be empty");
		}
		if(StringUtils.isBlank(dto.getItemUuid())) {
			return renderFail("ERROR: ItemUuid can not be empty");
		}
		if(StringUtils.isBlank(dto.getPlayerUuid())) {
			return renderFail("ERROR: PlayerUuid can not be empty");
		}
		if(dto.getAmount() == null) {
			return renderFail("ERROR: amount can not be empty");
		}
		
		return itemService.make(dto);
	}

}
