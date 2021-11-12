package com.sangria.operation.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sangria.operation.dao.GameManagerMapper;
import com.sangria.operation.dao.GameMapper;
import com.sangria.operation.dao.InventoryMapper;
import com.sangria.operation.dao.ItemMapper;
import com.sangria.operation.dto.InventoryAddDTO;
import com.sangria.operation.dto.InventoryClearDTO;
import com.sangria.operation.dto.InventoryInfoReturnDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.entity.GameManagerDO;
import com.sangria.operation.entity.InventoryDO;
import com.sangria.operation.entity.ItemDO;
import com.sangria.operation.service.InventoryService;
import com.sangria.operation.utils.CommonUtils;
/**
 * 
 * @author Steven Huang
 *
 */
@Service
public class InventoryServiceImpl implements InventoryService{
	
	@Resource
	private GameManagerMapper gameManagerMapper;
	
	@Resource
	private InventoryMapper inventoryMapper;
	
	@Resource
	private ItemMapper itemMapper;
	
	@Resource
	private GameMapper gameMapper;
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO add(InventoryAddDTO dto) {
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
		if(StringUtils.isBlank(gameUuid)) {
			return new ResponseDTO(500, "ERROR: this manager has not created a game yet", null);
		}
		
		InventoryDO inventory = new InventoryDO();
		inventory.setGameUuid(gameUuid);
		inventory.setCreateTime(CommonUtils.getTimeNow());
		inventory.setModifiedTime(CommonUtils.getTimeNow());
		inventory.setUuid(CommonUtils.generateUniqueId("INV", 3));
		
		if(inventoryMapper.insert(inventory) <= 0) {
			return new ResponseDTO(500, "ERROR: add inventory failed", null);
		}
		
		return new ResponseDTO(200, "Adding Inventory Success", null);
	}
	
	@Override
	public ResponseDTO clear(InventoryClearDTO dto) {
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
		item.setInventoryUuid(inventory.getUuid());
		
		if(itemMapper.delete(new QueryWrapper<>(item)) < 0) {
			return new ResponseDTO(500, "ERROR: inventory clear failed", null);
		}
		
		return new ResponseDTO(200, "inventory cleared", null);
		
	}
	
	@Override
	public ResponseDTO info(String token, String inventoryId) {
		List<InventoryInfoReturnDTO> returnList = new ArrayList<>();
		
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
		inventory.setUuid(inventoryId);
		inventory.setGameUuid(gameUuid);
		List<InventoryDO> inventoryList = inventoryMapper.selectList(new QueryWrapper<>(inventory));
		if(inventoryList == null || inventoryList.size() == 0) {
			return new ResponseDTO(500, "ERROR: can not find an inventory with inventoryID: " + inventoryId + " under the game you registered", null);
		}
		
		if(inventoryList != null && inventoryList.size() == 1) {
			inventory = inventoryList.get(0);
			ItemDO item = new ItemDO();
			item.setInventoryUuid(inventory.getUuid());
			List<ItemDO> itemList = itemMapper.selectList(new QueryWrapper<>(item)) == null ? new ArrayList<ItemDO>() : itemMapper.selectList(new QueryWrapper<>(item));
			
			InventoryInfoReturnDTO returnDTO = new InventoryInfoReturnDTO();
			returnDTO.setInventoryId(inventory.getUuid());
			returnDTO.setCreatedAt(inventory.getCreateTime());
			returnDTO.setItemCount(itemList.size());
			returnDTO.setItemList(itemList);
			
			return new ResponseDTO(200, "querying inventory infomation success", returnDTO);
		}else {
			for(InventoryDO inve : inventoryList) {
				ItemDO item = new ItemDO();
				item.setInventoryUuid(inve.getUuid());
				List<ItemDO> itemList = itemMapper.selectList(new QueryWrapper<>(item)) == null ? new ArrayList<ItemDO>() : itemMapper.selectList(new QueryWrapper<>(item));
				
				InventoryInfoReturnDTO returnDTO = new InventoryInfoReturnDTO();
				returnDTO.setInventoryId(inve.getUuid());
				returnDTO.setCreatedAt(inve.getCreateTime());
				returnDTO.setItemCount(itemList.size());
				returnDTO.setItemList(itemList);
				
				returnList.add(returnDTO);
				
			}
			return new ResponseDTO(200, "querying inventory information success", returnList);
			
		}	
	}
}
