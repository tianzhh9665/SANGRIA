package com.sangria.operation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sangria.operation.Enum.PlayerStatusEnum;
import com.sangria.operation.Enum.PlayerTradeTypeEnum;
import com.sangria.operation.Enum.PlayerTradeWayEnum;
import com.sangria.operation.dao.GameManagerMapper;
import com.sangria.operation.dao.InventoryMapper;
import com.sangria.operation.dao.PlayerInventoryMapper;
import com.sangria.operation.dao.PlayerMapper;
import com.sangria.operation.dto.PlayerAddDTO;
import com.sangria.operation.dto.PlayerDeleteDTO;
import com.sangria.operation.dto.PlayerFreezeDTO;
import com.sangria.operation.dto.PlayerInfoReturnDTO;
import com.sangria.operation.dto.PlayerTradeRequestDTO;
import com.sangria.operation.dto.PlayerUnfreezeDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.entity.GameManagerDO;
import com.sangria.operation.entity.InventoryDO;
import com.sangria.operation.entity.PlayerDO;
import com.sangria.operation.entity.PlayerInventoryDO;
import com.sangria.operation.service.PlayerService;
import com.sangria.operation.utils.CommonUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Resource
    private GameManagerMapper gameManagerMapper;

    @Resource
    private PlayerMapper playerMapper;
    
    @Resource
    private InventoryMapper inventoryMapper;
    
    @Resource 
    private PlayerInventoryMapper playerInventoryMapper;
    
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO add(PlayerAddDTO dto) {
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
		inventory.setUuid(dto.getInventoryUuid());
		inventory.setGameUuid(gameUuid);
		List<InventoryDO> inventoryList = inventoryMapper.selectList(new QueryWrapper<>(inventory));
		if(inventoryList == null || inventoryList.size() == 0) {
			return new ResponseDTO(500, "ERROR: can not find an inventory with inventoryID: " + dto.getInventoryUuid() + " under the game you registered", null);
		}
		if(inventoryList != null && inventoryList.size() > 1) {
			return new ResponseDTO(500, "ERROR: more than one inventory with inventoryID: " + dto.getInventoryUuid() + " found, please contact system admin", null);
		}
		
		inventory = inventoryList.get(0);
		
		PlayerDO player = new PlayerDO();
		String playerUuid = CommonUtils.generateUniqueId("PLAYER", 3);
		player.setUuid(playerUuid);
		player.setCreateTime(CommonUtils.getTimeNow());
		player.setModifiedTime(CommonUtils.getTimeNow());
		player.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		player.setBalance(0);
		player.setGameUuid(gameUuid);
		player.setGameInventoryUuid(dto.getInventoryUuid());
		
		if(playerMapper.insert(player) <= 0) {
			return new ResponseDTO(500, "ERROR: add player failed", null);
		}
		
		return new ResponseDTO(200, "add player success", playerUuid);
    }

    @Override
    public ResponseDTO info(String playerUuid, String token) {
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
        
        PlayerDO player = new PlayerDO();
        player.setUuid(playerUuid);
        player.setGameUuid(gameUuid);
        List<PlayerDO> playerList = playerMapper.selectList(new QueryWrapper<>(player));
        if(playerList == null || playerList.size() == 0) {
            return new ResponseDTO(500, "ERROR: can not find any player with playerUuid: " + playerUuid + " in the game with GameUuid: " + gameUuid, null);
        }
        if(managerList != null && managerList.size() > 1) {
            return new ResponseDTO(500, "ERROR: more than one player found with the same playerUuid, please try again later", null);
        }
        
        player = playerList.get(0);
        PlayerInfoReturnDTO returnDTO = CommonUtils.copyData(player, PlayerInfoReturnDTO.class);
        returnDTO.setStatus(returnDTO.getStatus().equals(PlayerStatusEnum.NORMAL.getStatus()) ? PlayerStatusEnum.NORMAL.getMessage() : PlayerStatusEnum.FROZEN.getMessage());
        
        return new ResponseDTO(200, "query player info success", returnDTO);
        
    }
    
    @Override
    public ResponseDTO freeze(PlayerFreezeDTO dto) {
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

        PlayerDO playerSearch = new PlayerDO();
        playerSearch.setUuid(dto.getPlayerId());
        playerSearch.setGameUuid(gameUuid);
        playerSearch.setStatus("1");

        PlayerDO player = playerMapper.selectOne(new QueryWrapper<>(playerSearch));
        if (player == null) {
            return new ResponseDTO(500, "ERROR: the player has already been frozen or there is no qualified player", null);
        }

        player.setStatus("2");

        playerMapper.updateById(player);

        return new ResponseDTO(200, "player frozen successfully", null);
    }

    @Override
    public ResponseDTO unfreeze(PlayerUnfreezeDTO dto) {
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

        PlayerDO playerSearch = new PlayerDO();
        playerSearch.setUuid(dto.getPlayerId());
        playerSearch.setGameUuid(gameUuid);
        playerSearch.setStatus("2");

        PlayerDO player = playerMapper.selectOne(new QueryWrapper<>(playerSearch));
        if (player == null) {
            return new ResponseDTO(500, "ERROR: the player has already been unfrozen or there is no qualified player", null);
        }

        player.setStatus("1");

        playerMapper.updateById(player);

        return new ResponseDTO(200, "player unfrozen successfully", null);
    }

    @Override
    public ResponseDTO deletePlayer(PlayerDeleteDTO dto){
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

        PlayerDO playerDeleted = new PlayerDO();
        playerDeleted.setUuid(dto.getPlayerId());
        playerDeleted.setGameUuid(gameUuid);

        List<PlayerDO> playerList = playerMapper.selectList(new QueryWrapper<>(playerDeleted));
        if(playerList == null || playerList.size() == 0) {
            return new ResponseDTO(500, "ERROR: there is no qualified player, try again", null);
        }
        if(playerList != null && playerList.size() > 1) {
            return new ResponseDTO(500, "ERROR: more than one player found, please try again later", null);
        }

        playerDeleted = playerList.get(0);

        if(playerMapper.delete(new QueryWrapper<>(playerDeleted)) <= 0) {
            return new ResponseDTO(500, "ERROR: no player has been deleted", null);
        }

        return new ResponseDTO(200, "player has been deleted successfully", null);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    public ResponseDTO trade(PlayerTradeRequestDTO dto) {
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
        
        if(dto.getType() == PlayerTradeTypeEnum.MONEY_TO_MONEY.getType()) {
        	
        	PlayerDO player =  new PlayerDO();
        	player.setUuid(dto.getPlayer1Uuid());
        	PlayerDO player1 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player1 == null) {
        		return new ResponseDTO(500, "ERROR: player 1 can not be found", null);
        	}
        	if(player1.getBalance().intValue() < dto.getPlayer1Amount().intValue()) {
        		return new ResponseDTO(500, "ERROR: insufficient balance for player 1", null);
        	}
        	
        	player =  new PlayerDO();
        	player.setUuid(dto.getPlayer2Uuid());
        	PlayerDO player2 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player2 == null) {
        		return new ResponseDTO(500, "ERROR: player 2 can not be found", null);
        	}
        	if(player2.getBalance().intValue() < dto.getPlayer2Amount().intValue()) {
        		return new ResponseDTO(500, "ERROR: insufficient balance for player 2", null);
        	}
        	
        	ResponseDTO p1Decrease = tradeMoney(dto.getPlayer1Uuid(),dto.getPlayer1Amount().intValue(),PlayerTradeWayEnum.DECREASE.getTradeWay());
        	if(p1Decrease.getCode() != 200) {
        		return p1Decrease;
        	}
        	ResponseDTO p1Increase = tradeMoney(dto.getPlayer1Uuid(),dto.getPlayer2Amount().intValue(),PlayerTradeWayEnum.INCREASE.getTradeWay());
        	if(p1Decrease.getCode() != 200) {
        		return p1Increase;
        	}
        	ResponseDTO p2Decrease = tradeMoney(dto.getPlayer2Uuid(),dto.getPlayer2Amount().intValue(),PlayerTradeWayEnum.DECREASE.getTradeWay());
        	if(p1Decrease.getCode() != 200) {
        		return p2Decrease;
        	}
        	ResponseDTO p2Increase = tradeMoney(dto.getPlayer2Uuid(),dto.getPlayer1Amount().intValue(),PlayerTradeWayEnum.INCREASE.getTradeWay());
        	if(p1Decrease.getCode() != 200) {
        		return p2Increase;
        	}
        	
        	return new ResponseDTO(200, "trade complete", null);
        }else if(dto.getType() == PlayerTradeTypeEnum.MONEY_TO_ITEM.getType()) {
        	PlayerDO player =  new PlayerDO();
        	player.setUuid(dto.getPlayer1Uuid());
        	PlayerDO player1 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player1 == null) {
        		return new ResponseDTO(500, "ERROR: player 1 can not be found", null);
        	}
        	if(player1.getBalance().intValue() < dto.getPlayer1Amount().intValue()) {
        		return new ResponseDTO(500, "ERROR: insufficient balance for player 1", null);
        	}
        	
        	player =  new PlayerDO();
        	player.setUuid(dto.getPlayer2Uuid());
        	PlayerDO player2 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player2 == null) {
        		return new ResponseDTO(500, "ERROR: player 2 can not be found", null);
        	}
        	
        	PlayerInventoryDO playerInv = new PlayerInventoryDO();
    		playerInv.setPlayerUuid(player2.getUuid());
    		List<PlayerInventoryDO> playerInvList = playerInventoryMapper.selectList(new QueryWrapper<>(playerInv));
    		if(playerInvList == null || playerInvList.size() == 0) {
    			return new ResponseDTO(500, "ERROR: The player has no item in the inventory", null);
    		}
    		
    		PlayerInventoryDO playerInv1 = new PlayerInventoryDO();
    		playerInv1.setPlayerUuid(player1.getUuid());
    		List<PlayerInventoryDO> playerInvList1 = playerInventoryMapper.selectList(new QueryWrapper<>(playerInv1));
    		
    		int sufficient = 0;
    		List<List> tradeList= dto.getPlayer2ItemList();
    		for(List tradeDetail : tradeList) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() >= amount) {
    						sufficient +=1;
    					}else {
    						return new ResponseDTO(500, "ERROR: insufficient ingredient: ItemUuid: " + itemID + ", required: " + String.valueOf(amount) + ", having: " + String.valueOf(inv.getAmount()), null);
    					}
    				}
    			}
    		}
    		if(sufficient != tradeList.size()) {
    			return new ResponseDTO(500,"insufficient items in the inventory to trade",null);
    		}
    		
    		//all check completed, start trading
    		ResponseDTO p1Decrease = tradeMoney(dto.getPlayer1Uuid(),dto.getPlayer1Amount().intValue(),PlayerTradeWayEnum.DECREASE.getTradeWay());
        	if(p1Decrease.getCode() != 200) {
        		return p1Decrease;
        	}
        	ResponseDTO p2Increase = tradeMoney(dto.getPlayer2Uuid(),dto.getPlayer1Amount().intValue(),PlayerTradeWayEnum.INCREASE.getTradeWay());
        	if(p2Increase.getCode() != 200) {
        		return p2Increase;
        	}
        	for(List tradeDetail : tradeList) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() - amount == 0) {
    						playerInventoryMapper.deleteById(inv);
    					}else {
    						inv.setAmount(inv.getAmount().intValue() - amount);
        					playerInventoryMapper.updateById(inv);
    					}
    					
    					boolean found = false;
    					for(PlayerInventoryDO inv1 : playerInvList1) {
    						if(inv1.getItemUuid().equals(itemID)) {
    							found = true;
    							inv1.setAmount(inv1.getAmount().intValue() + amount);
    							playerInventoryMapper.updateById(inv1);
    						}
    					}
    					
    					if(!found) {
        					PlayerInventoryDO p1Item = new PlayerInventoryDO();
        					p1Item.setUuid(CommonUtils.generateUniqueId("PINV", 3));
        					p1Item.setItemUuid(itemID);
        					p1Item.setAmount(amount);
        					p1Item.setPlayerUuid(dto.getPlayer1Uuid());
        					p1Item.setCreateTime(CommonUtils.getTimeNow());
        					p1Item.setModifiedTime(CommonUtils.getTimeNow());
        					
        					playerInventoryMapper.insert(p1Item);
    					}
    				}
    			}
    		}
        	return new ResponseDTO(200, "trade complete", null);
        }else if(dto.getType() == PlayerTradeTypeEnum.ITEM_TO_MONEY.getType()) {
        	PlayerDO player =  new PlayerDO();
        	player.setUuid(dto.getPlayer2Uuid());
        	PlayerDO player2 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player2 == null) {
        		return new ResponseDTO(500, "ERROR: player 2 can not be found", null);
        	}
        	if(player2.getBalance().intValue() < dto.getPlayer2Amount().intValue()) {
        		return new ResponseDTO(500, "ERROR: insufficient balance for player 2", null);
        	}
        	
        	player =  new PlayerDO();
        	player.setUuid(dto.getPlayer1Uuid());
        	PlayerDO player1 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player1 == null) {
        		return new ResponseDTO(500, "ERROR: player 1 can not be found", null);
        	}
        	
        	PlayerInventoryDO playerInv = new PlayerInventoryDO();
    		playerInv.setPlayerUuid(player1.getUuid());
    		List<PlayerInventoryDO> playerInvList = playerInventoryMapper.selectList(new QueryWrapper<>(playerInv));
    		if(playerInvList == null || playerInvList.size() == 0) {
    			return new ResponseDTO(500, "ERROR: The player has no item in the inventory", null);
    		}
    		
    		PlayerInventoryDO playerInv2 = new PlayerInventoryDO();
    		playerInv2.setPlayerUuid(player2.getUuid());
    		List<PlayerInventoryDO> playerInvList2 = playerInventoryMapper.selectList(new QueryWrapper<>(playerInv2));
    		
    		int sufficient = 0;
    		List<List> tradeList= dto.getPlayer1ItemList();
    		for(List tradeDetail : tradeList) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() >= amount) {
    						sufficient +=1;
    					}else {
    						return new ResponseDTO(500, "ERROR: insufficient ingredient: ItemUuid: " + itemID + ", required: " + String.valueOf(amount) + ", having: " + String.valueOf(inv.getAmount()), null);
    					}
    				}
    			}
    		}
    		if(sufficient != tradeList.size()) {
    			return new ResponseDTO(500,"insufficient items in the inventory to trade",null);
    		}
    		
    		//all check completed, start trading
    		ResponseDTO p2Decrease = tradeMoney(dto.getPlayer2Uuid(),dto.getPlayer2Amount().intValue(),PlayerTradeWayEnum.DECREASE.getTradeWay());
        	if(p2Decrease.getCode() != 200) {
        		return p2Decrease;
        	}
        	ResponseDTO p1Increase = tradeMoney(dto.getPlayer1Uuid(),dto.getPlayer2Amount().intValue(),PlayerTradeWayEnum.INCREASE.getTradeWay());
        	if(p1Increase.getCode() != 200) {
        		return p1Increase;
        	}
        	for(List tradeDetail : tradeList) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() - amount == 0) {
    						playerInventoryMapper.deleteById(inv);
    					}else {
    						inv.setAmount(inv.getAmount().intValue() - amount);
        					playerInventoryMapper.updateById(inv);
    					}
    					
    					boolean found = false;
    					for(PlayerInventoryDO inv2 : playerInvList2) {
    						if(inv2.getItemUuid().equals(itemID)) {
    							found = true;
    							inv2.setAmount(inv2.getAmount().intValue() + amount);
    							playerInventoryMapper.updateById(inv2);
    						}
    					}
    					
    					if(!found) {
        					PlayerInventoryDO p2Item = new PlayerInventoryDO();
        					p2Item.setUuid(CommonUtils.generateUniqueId("PINV", 3));
        					p2Item.setItemUuid(itemID);
        					p2Item.setAmount(amount);
        					p2Item.setPlayerUuid(dto.getPlayer2Uuid());
        					p2Item.setCreateTime(CommonUtils.getTimeNow());
        					p2Item.setModifiedTime(CommonUtils.getTimeNow());
        					
        					playerInventoryMapper.insert(p2Item);
    					}
    				}
    			}
    		}
        	return new ResponseDTO(200, "trade complete", null);
        	
        }else if(dto.getType() == PlayerTradeTypeEnum.ITEM_TO_ITEM.getType()) {
        	PlayerDO player =  new PlayerDO();
        	player.setUuid(dto.getPlayer1Uuid());
        	PlayerDO player1 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player1 == null) {
        		return new ResponseDTO(500, "ERROR: player 1 can not be found", null);
        	}
        	
        	player =  new PlayerDO();
        	player.setUuid(dto.getPlayer2Uuid());
        	PlayerDO player2 = playerMapper.selectOne(new QueryWrapper<>(player));
        	if(player2 == null) {
        		return new ResponseDTO(500, "ERROR: player 2 can not be found", null);
        	}
        	
        	//check player 1's inventory
        	PlayerInventoryDO playerInv = new PlayerInventoryDO();
    		playerInv.setPlayerUuid(player1.getUuid());
    		List<PlayerInventoryDO> playerInvList = playerInventoryMapper.selectList(new QueryWrapper<>(playerInv));
    		if(playerInvList == null || playerInvList.size() == 0) {
    			return new ResponseDTO(500, "ERROR: The player has no item in the inventory", null);
    		}
    		int sufficient = 0;
    		List<List> tradeList = dto.getPlayer1ItemList();
    		for(List tradeDetail : tradeList) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() >= amount) {
    						sufficient +=1;
    					}else {
    						return new ResponseDTO(500, "ERROR: insufficient ingredient: ItemUuid: " + itemID + ", required: " + String.valueOf(amount) + ", having: " + String.valueOf(inv.getAmount()), null);
    					}
    				}
    			}
    		}
    		if(sufficient != tradeList.size()) {
    			return new ResponseDTO(500,"insufficient items in the inventory to trade",null);
    		}
        	
        	//check player 2's inventory
    		sufficient = 0;
    		PlayerInventoryDO playerInv2 = new PlayerInventoryDO();
    		playerInv2.setPlayerUuid(player2.getUuid());
    		List<PlayerInventoryDO> playerInvList2 = playerInventoryMapper.selectList(new QueryWrapper<>(playerInv2));
    		if(playerInvList2 == null || playerInvList2.size() == 0) {
    			return new ResponseDTO(500, "ERROR: The player has no item in the inventory", null);
    		}
    		
    		List<List> tradeList2 = dto.getPlayer2ItemList();
    		for(List tradeDetail : tradeList2) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList2) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() >= amount) {
    						sufficient +=1;
    					}else {
    						return new ResponseDTO(500, "ERROR: insufficient ingredient: ItemUuid: " + itemID + ", required: " + String.valueOf(amount) + ", having: " + String.valueOf(inv.getAmount()), null);
    					}
    				}
    			}
    		}
    		if(sufficient != tradeList2.size()) {
    			return new ResponseDTO(500,"insufficient items in the inventory to trade",null);
    		}
    		
        	//remove from p1 add to p2
    		for(List tradeDetail : tradeList) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() - amount == 0) {
    						playerInventoryMapper.deleteById(inv);
    					}else {
        					inv.setAmount(inv.getAmount().intValue() - amount);
        					playerInventoryMapper.updateById(inv);
    					}
    					
    					boolean found = false;
    					for(PlayerInventoryDO inv2 : playerInvList2) {
    						if(inv2.getItemUuid().equals(itemID)) {
    							found = true;
    							inv2.setAmount(inv2.getAmount().intValue() + amount);
    							playerInventoryMapper.updateById(inv2);
    						}
    					}
    					
    					if(!found) {
        					PlayerInventoryDO p2Item = new PlayerInventoryDO();
        					p2Item.setUuid(CommonUtils.generateUniqueId("PINV", 3));
        					p2Item.setItemUuid(itemID);
        					p2Item.setAmount(amount);
        					p2Item.setPlayerUuid(dto.getPlayer2Uuid());
        					p2Item.setCreateTime(CommonUtils.getTimeNow());
        					p2Item.setModifiedTime(CommonUtils.getTimeNow());
        					
        					playerInventoryMapper.insert(p2Item);
    					}
    				}
    			}
    		}
        	
        	//remove from p2 add to p1
    		for(List tradeDetail : tradeList2) {
    			String itemID = (String)tradeDetail.get(0);
    			int amount = (int)tradeDetail.get(1);
    			
    			for(PlayerInventoryDO inv : playerInvList2) {
    				if(inv.getItemUuid().equals(itemID)) {
    					if(inv.getAmount().intValue() - amount == 0) {
    						playerInventoryMapper.deleteById(inv);
    					}else {
        					inv.setAmount(inv.getAmount().intValue() - amount);
        					playerInventoryMapper.updateById(inv);
    					}

    					boolean found = false;
    					for(PlayerInventoryDO inv2 : playerInvList) {
    						if(inv2.getItemUuid().equals(itemID)) {
    							found = true;
    							inv2.setAmount(inv2.getAmount().intValue() + amount);
    							playerInventoryMapper.updateById(inv2);
    						}
    					}
    					if(!found) {
    						PlayerInventoryDO p1Item = new PlayerInventoryDO();
        					p1Item.setUuid(CommonUtils.generateUniqueId("PINV", 3));
        					p1Item.setItemUuid(itemID);
        					p1Item.setAmount(amount);
        					p1Item.setPlayerUuid(dto.getPlayer1Uuid());
        					p1Item.setCreateTime(CommonUtils.getTimeNow());
        					p1Item.setModifiedTime(CommonUtils.getTimeNow());
        					
        					playerInventoryMapper.insert(p1Item);
    					}
    				}
    			}
    		}
    		
    		return new ResponseDTO(200, "trade complete", null);
        }else {
        	return new ResponseDTO(500, "ERROR: Invalid trade type", null);
        }
    }
    /**
     * playerUuid should be check valid before passing in
     * @param playerUuid
     * @param amount
     * @param tradeWay
     * @return
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    ResponseDTO tradeMoney(String playerUuid, int amount, int tradeWay) {
    	PlayerDO player =  new PlayerDO();
    	player.setUuid(playerUuid);
    	PlayerDO playerFound = playerMapper.selectOne(new QueryWrapper<>(player));
    	
    	if(playerFound == null) {
    		return new ResponseDTO(501, "ERROR: trade player not found",null);
    	}
    	if(playerFound.getStatus().equals(PlayerStatusEnum.FROZEN.getStatus())) {
    		return new ResponseDTO(502, "ERROR: player is frozen",null);
    	}
    	if(tradeWay == PlayerTradeWayEnum.DECREASE.getTradeWay()) {
    		if(amount > playerFound.getBalance().intValue()) {
    			return new ResponseDTO(504, "ERROR: insufficient balance for player with playerUuid: " + playerUuid,null);
    		}
    		playerFound.setBalance(playerFound.getBalance().intValue() - amount);
    		if(playerMapper.updateById(playerFound) < 0) {
    			return new ResponseDTO(505, "ERROR: decrease balance failed with playerUuid: " + playerUuid,null);
    		}
    		return new ResponseDTO(200, "completed",null);
    	}else if(tradeWay == PlayerTradeWayEnum.INCREASE.getTradeWay()) {
    		playerFound.setBalance(playerFound.getBalance().intValue() + amount);
    		if(playerMapper.updateById(playerFound) < 0) {
    			return new ResponseDTO(505, "ERROR: increase balance failed with playerUuid: " + playerUuid,null);
    		}
    		return new ResponseDTO(200, "completed",null);
    	}else {
    		return new ResponseDTO(503, "ERROR: invalid tradeWay",null);
    	}
    }
    
}
