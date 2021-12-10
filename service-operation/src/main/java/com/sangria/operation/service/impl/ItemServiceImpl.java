package com.sangria.operation.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sangria.operation.Enum.ItemTypeEnum;
import com.sangria.operation.Enum.PlayerStatusEnum;
import com.sangria.operation.dao.GameManagerMapper;
import com.sangria.operation.dao.InventoryMapper;
import com.sangria.operation.dao.ItemMapper;
import com.sangria.operation.dao.PlayerInventoryMapper;
import com.sangria.operation.dao.PlayerMapper;
import com.sangria.operation.dto.ItemAddDTO;
import com.sangria.operation.dto.ItemMakeDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.entity.GameManagerDO;
import com.sangria.operation.entity.InventoryDO;
import com.sangria.operation.entity.ItemDO;
import com.sangria.operation.entity.PlayerDO;
import com.sangria.operation.entity.PlayerInventoryDO;
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
	
	@Resource
	private PlayerMapper playerMapper;
	
	@Resource
	private PlayerInventoryMapper playerInventoryMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
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
		if(StringUtils.isBlank(gameUuid)) {
            return new ResponseDTO(500, "ERROR: this manager has not created a game yet", null);
        }
		
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
		item.setDescription(dto.getDescription());
		item.setCreateTime(CommonUtils.getTimeNow());
		item.setModifiedTime(CommonUtils.getTimeNow());
		item.setUuid(CommonUtils.generateUniqueId("ITEM", 3));
		
		if(dto.getType() == ItemTypeEnum.COM_AND_NOT_TRA.getType() || dto.getType() == ItemTypeEnum.NOT_COM_AND_NOT_TRA.getType()) {
			item.setPrice(0);
		}else {
			item.setPrice(dto.getPrice());
		}
		try {
			if(dto.getType() == ItemTypeEnum.COM_AND_NOT_TRA.getType() || dto.getType() == ItemTypeEnum.COM_AND_TRA.getType()) {
				String ingredientStr = "";
				List<List> ingredientList = dto.getIngredients();
				for(List ingredient : ingredientList) {
					if(ingredient.size() != 2) {
						return new ResponseDTO(500, "ERROR: invalid indredient format", null);
					}
					String itemUuid = (String)ingredient.get(0);
					int amount = (int)ingredient.get(1);
					
					ItemDO itemSearch = new ItemDO();
					itemSearch.setInventoryUuid(dto.getInventoryId());
					itemSearch.setUuid(itemUuid);
					
					List<ItemDO> found = itemMapper.selectList(new QueryWrapper<>(itemSearch));
					if(found == null || found.size() == 0) {
						return new ResponseDTO(500, "ERROR: no item found with itemID: " + itemUuid + " in inventory with inventoryID: " + dto.getInventoryId(), null);
					}
					ingredientStr += itemUuid + ":" + String.valueOf(amount) + ",";
				}
				ingredientStr = ingredientStr.substring(0, ingredientStr.length()-1);
				item.setIngredients(ingredientStr);
			}
		}catch (Exception e) {
			return new ResponseDTO(500, "ERROR: error occurs when converting ingredients list", null);
		}
		
		
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
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO make(ItemMakeDTO dto) {
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
		
		//check if player exists
		PlayerDO player = new PlayerDO();
		player.setUuid(dto.getPlayerUuid());
		player.setGameUuid(gameUuid);
		
		List<PlayerDO> playerCheck = playerMapper.selectList(new QueryWrapper<>(player));
		if(playerCheck == null || playerCheck.size() == 0) {
			return new ResponseDTO(500, "ERROR: can not find any player with given playerUuid in your managed game", null);
		}
		if(playerCheck != null && playerCheck.size() > 1) {
			return new ResponseDTO(500, "ERROR: more than one player found, please try again later", null);
		}
		
		player = playerCheck.get(0);
		if(player.getStatus().equals(PlayerStatusEnum.FROZEN.getStatus())) {
			return new ResponseDTO(500, "ERROR: Player is currently frozen, please unfreeze it first", null);
		}
		//check if item exists in the player's associated game inventory
		
		ItemDO item = new ItemDO();
		item.setUuid(dto.getItemUuid());
		item.setInventoryUuid(player.getGameInventoryUuid());
		List<ItemDO> itemCheck = itemMapper.selectList(new QueryWrapper<>(item));
		if(itemCheck == null || itemCheck.size() == 0) {
			return new ResponseDTO(500, "ERROR: can not find any item with given ItemUuid in this player associated inventory", null);
		}
		if(itemCheck != null && itemCheck.size() > 1) {
			return new ResponseDTO(500, "ERROR: more than one item found, please try again later", null);
		}
		
		item = itemCheck.get(0);
		if(item.getType() == ItemTypeEnum.NOT_COM_AND_NOT_TRA.getType() || item.getType() == ItemTypeEnum.NOT_COM_AND_TRA.getType()) {
			return new ResponseDTO(500, "ERROR: This item is not compostable", null);
		}
		
		//get the player's inventory info
		PlayerInventoryDO playerInv = new PlayerInventoryDO();
		playerInv.setPlayerUuid(dto.getPlayerUuid());
		List<PlayerInventoryDO> playerInvList = playerInventoryMapper.selectList(new QueryWrapper<>(playerInv));
		if(playerInvList == null || playerInvList.size() == 0) {
			return new ResponseDTO(500, "ERROR: The player has no item in the inventory", null);
		}
		
		//get the ingredients info of the item
		String ingredientStr = item.getIngredients();
		String[] itemArray = ingredientStr.split(",");
		int sufficient = 0;
		for(String ingredient : itemArray) {
			String ItemId = ingredient.split(":")[0];
			int amount = Integer.parseInt(ingredient.split(":")[1]) * dto.getAmount();
			//check player's inventory for sufficient ingredients
			for(PlayerInventoryDO InvItem : playerInvList) {
				if(InvItem.getItemUuid().equals(ItemId)) {
					if(InvItem.getAmount() >= amount) {
						sufficient += 1;
					}else {
						return new ResponseDTO(500, "ERROR: insufficient ingredient: ItemUuid: " + ItemId + ", required: " + String.valueOf(amount) + ", having: " + String.valueOf(InvItem.getAmount()), null);
					}
				}
			}
		}
		if(sufficient != itemArray.length) {
			return new ResponseDTO(500, "ERROR: insufficient ingredients", null);
		}
		
		//remove ingredient and add the new item
		for(int i = 0; i < itemArray.length; i++) {
			String ItemId = itemArray[i].split(":")[0];
			int amount = Integer.parseInt(itemArray[i].split(":")[1]) * dto.getAmount();
			playerInv = new PlayerInventoryDO();
			playerInv.setPlayerUuid(dto.getPlayerUuid());
			playerInv.setItemUuid(ItemId);
			
			PlayerInventoryDO playerInvItem = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInv));
			if(playerInvItem != null) {
				playerInvItem.setAmount(playerInvItem.getAmount() - amount);
				if(playerInventoryMapper.updateById(playerInvItem) < 0) {
					return new ResponseDTO(500, "ERROR: remove ingredients failed", null);
				}
			}
		}
		boolean found = false;
		for(PlayerInventoryDO inv: playerInvList) {
			if(inv.getItemUuid().equals(dto.getItemUuid())) {
				found = true;
				inv.setAmount(inv.getAmount().intValue() + dto.getAmount().intValue());
				if(playerInventoryMapper.updateById(inv) <= 0) {
					return new ResponseDTO(500, "ERROR: update item amount failed", null);
				}
			}
		}
		if(!found) {
			playerInv = new PlayerInventoryDO();
			playerInv.setPlayerUuid(dto.getPlayerUuid());
			playerInv.setUuid(CommonUtils.generateUniqueId("PINV", 3));
			playerInv.setItemUuid(dto.getItemUuid());
			playerInv.setAmount(dto.getAmount());
			playerInv.setCreateTime(CommonUtils.getTimeNow());
			playerInv.setModifiedTime(CommonUtils.getTimeNow());
			
			if(playerInventoryMapper.insert(playerInv) <= 0) {
				return new ResponseDTO(500, "ERROR: insert new item failed", null);
			}
		}
		return new ResponseDTO(200, "item make success", null);
	}
}
