package com.sangria.operation.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangria.operation.dao.GameManagerMapper;
import com.sangria.operation.dao.InventoryMapper;
import com.sangria.operation.dao.ItemMapper;
import com.sangria.operation.dto.ItemAddDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.entity.GameManagerDO;
import com.sangria.operation.entity.InventoryDO;
import com.sangria.operation.entity.ItemDO;
import com.sangria.operation.service.ItemService;
import com.sangria.operation.utils.CommonUtils;
/**
 * 
 * @author Steven Huang
 *
 */
@Service
public class ItemServiceImpl implements ItemService{
	
	@Resource
	private ItemMapper itemMapper;
	
	@Resource
	private GameManagerMapper gameManagerMapper;
	
	@Resource
	private InventoryMapper inventoryMapper;
	
	@Override
	public ResponseDTO add(ItemAddDTO dto) {
		String token = dto.getToken();
		GameManagerDO manager = new GameManagerDO();
		manager.setToken(token);
		
		List<GameManagerDO> managerList = gameManagerMapper.selectList(new QueryWrapper<>(manager));
		if(managerList == null || managerList.size() == 0) {
			return new ResponseDTO(500, "ERROR: token is not valid, please login first", null);
		}
		if(managerList != null && managerList.size() > 1) {
			return new ResponseDTO(500, "ERROR: more than one manager found, please try again later", null);
		}
		
		manager = managerList.get(0);
		String gameUuid = manager.getGameUuid();
		
		InventoryDO inventory = new InventoryDO();
		inventory.setUuid(dto.getInventoryId());
		inventory.setGameUuid(gameUuid);
		List<InventoryDO> inventoryList = inventoryMapper.selectList(new QueryWrapper<>(inventory));
		if(inventoryList == null || inventoryList.size() == 0) {
			return new ResponseDTO(500, "ERROR: can not find an inventory with inventoryID: " + dto.getInventoryId() + " under the game you registered", null);
		}
		if(inventoryList != null && inventoryList.size() > 1) {
			return new ResponseDTO(500, "ERROR: more than one inventory with inventoryID: " + dto.getInventoryId() + " found, please contact system admin", null);
		}
		
		inventory = inventoryList.get(0);
		
		ItemDO item = new ItemDO();
		item.setName(dto.getName());
		item.setType(dto.getType());
		item.setInventoryUuid(dto.getInventoryId());
		item.setAttributes(dto.getAttributes());
		item.setCreateTime(CommonUtils.getTimeNow());
		item.setModifiedTime(CommonUtils.getTimeNow());
		item.setUuid(CommonUtils.generateUniqueId("ITEM", 3));
		
		if(itemMapper.insert(item) <= 0) {
			return new ResponseDTO(500, "ERROR: item add failed", null);
		}
		
		return new ResponseDTO(200, "item successfully added", null);
	}

	@Override
	public ResponseDTO info(String token, String itemUuid) {
		GameManagerDO manager = new GameManagerDO();
		manager.setToken(token);
		
		List<GameManagerDO> managerList = gameManagerMapper.selectList(new QueryWrapper<>(manager));
		if(managerList == null || managerList.size() == 0) {
			return new ResponseDTO(500, "ERROR: token is not valid, please login first", null);
		}
		if(managerList != null && managerList.size() > 1) {
			return new ResponseDTO(500, "ERROR: more than one manager found, please try again later", null);
		}
		
		manager = managerList.get(0);
		InventoryDO inventory = new InventoryDO();;
		inventory.setGameUuid(manager.getGameUuid());
		List<InventoryDO> inventoryList = inventoryMapper.selectList(new QueryWrapper<>(inventory));
		
		if(inventoryList == null || inventoryList.size() == 0) {
			return new ResponseDTO(500, "ERROR: can not find any inventory in your registered game", null);
		}
		
		for(InventoryDO inv : inventoryList) {
			ItemDO item = new ItemDO();
			item.setUuid(itemUuid);
			item.setInventoryUuid(inv.getUuid());
			List<ItemDO> itemList = itemMapper.selectList(new QueryWrapper<>(item));
			
			if(itemList != null && itemList.size() > 0) {
				return new ResponseDTO(200, "querying item infomation succeed", itemList.get(0));
			}
		}
		
		return new ResponseDTO(500, "ERROR: can not find any item with itemId: " + itemUuid + " in any of your game inventory", null);
	}
}
