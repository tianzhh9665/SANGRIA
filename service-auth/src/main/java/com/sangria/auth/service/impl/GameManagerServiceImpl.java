package com.sangria.auth.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangria.auth.dao.GameManagerMapper;
import com.sangria.auth.dao.GameMapper;
import com.sangria.auth.dao.InventoryMapper;
import com.sangria.auth.dao.ItemMapper;
import com.sangria.auth.dto.ManagerDeleteDTO;
import com.sangria.auth.dto.ManagerLoginDTO;
import com.sangria.auth.dto.ManagerRegDTO;
import com.sangria.auth.dto.ResponseDTO;
import com.sangria.auth.entity.GameDO;
import com.sangria.auth.entity.GameManagerDO;
import com.sangria.auth.entity.InventoryDO;
import com.sangria.auth.entity.ItemDO;
import com.sangria.auth.service.GameManagerService;
import com.sangria.auth.utils.CommonUtils;
import com.sangria.auth.utils.MD5Utils;
import com.sangria.auth.utils.StringUtils;

@Service
public class GameManagerServiceImpl implements GameManagerService{
	
	@Resource
	private GameManagerMapper gameManagerMapper;
	
	@Resource
	private GameMapper gameMapper;
	
	@Resource
	private InventoryMapper inventoryMapper;

	@Resource
	private ItemMapper itemMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO register(ManagerRegDTO dto) {
		//check if the username already exists
		GameManagerDO managerSearch = new GameManagerDO();
		managerSearch.setUsername(dto.getUsername());
		List<GameManagerDO> managerSearchResult = gameManagerMapper.selectList(new QueryWrapper<>(managerSearch));
		if(managerSearchResult != null && managerSearchResult.size() >= 1) {
			return new ResponseDTO(500, "ERROR: username already exists, please choose another one", null);
		}
		
		//check if the gameName already exists
		String gameName = dto.getGameName();
		GameDO gameSearch = new GameDO();
		gameSearch.setName(gameName);
		List<GameDO> gameSearchResult = gameMapper.selectList(new QueryWrapper<>(gameSearch));
		if(gameSearchResult != null && gameSearchResult.size() >= 1) {
			return new ResponseDTO(500, "ERROR: The game you want to register already had a manager", null);
		}
		
		//create new managerDO and insert
		String managerUuid = CommonUtils.generateUniqueId("MGR", 3);
		String gameUuid = CommonUtils.generateUniqueId("GAME", 3);
		
		GameManagerDO manager = CommonUtils.copyData(dto, GameManagerDO.class);
		if(StringUtils.isBlank(manager.getUsername())) {
			manager.setUsername(dto.getUsername());
		}
		if(StringUtils.isBlank(manager.getPassword())) {
			manager.setPassword(dto.getPassword());
		}
		
		String encryptedPass = MD5Utils.getMD5(manager.getPassword());
		manager.setPassword(encryptedPass);
		manager.setUuid(managerUuid);
		manager.setGameUuid(gameUuid);
		manager.setCreateTime(CommonUtils.getTimeNow());
		manager.setModifiedTime(CommonUtils.getTimeNow());
		
		if(gameManagerMapper.insert(manager) <= 0) {
			return new ResponseDTO(500,"ERROR: Creating Manager Failed!", null);
		}
		//create new gameDO and insert
		GameDO game = new GameDO();
		game.setUuid(gameUuid);
		game.setName(dto.getGameName());
		game.setManagerUuid(managerUuid);
		game.setCreateTime(CommonUtils.getTimeNow());
		game.setModifiedTime(CommonUtils.getTimeNow());
		
		if(gameMapper.insert(game) <= 0) {
			return new ResponseDTO(500,"ERROR: Creating Manager Failed When Creating Game Info!", null);
		}
		
		return new ResponseDTO(200,"Registration Success", null);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO login(ManagerLoginDTO dto) {
		//check if the username already exists
		GameManagerDO managerSearch = new GameManagerDO();
		managerSearch.setUsername(dto.getUsername());
		List<GameManagerDO> managerSearchResult = gameManagerMapper.selectList(new QueryWrapper<>(managerSearch));
		if(managerSearchResult == null || managerSearchResult.size() == 0) {
			return new ResponseDTO(500, "ERROR: manager with username: " + dto.getUsername() + " does not exists, please register first", null);
		}
		if(managerSearchResult != null && managerSearchResult.size() > 1) {
			return new ResponseDTO(500, "ERROR: more that one manager with username: " + dto.getUsername() + " , please contact system admin", null);
		}
		//check if that manager already logged in
		GameManagerDO manager = managerSearchResult.get(0);
		if(StringUtils.isNotEmpty(manager.getToken())) {
			return new ResponseDTO(500, "ERROR: manager " + dto.getUsername() + " already logged in", null);
		}
		
		String encryptedPass = MD5Utils.getMD5(dto.getPassword());
		if(!encryptedPass.equals(manager.getPassword())) {
			return new ResponseDTO(500, "ERROR: incorrect password, please try again", null);
		}
		//assign token to the user and insert login infomation
		String token = UUID.randomUUID().toString().replace("-","");
		manager.setToken(token);
		manager.setModifiedTime(CommonUtils.getTimeNow());
		if(gameManagerMapper.updateById(manager) < 0) {
			return new ResponseDTO(500, "ERROR: login failed, please try again later", null);
		}
		
		return new ResponseDTO(200,"Login Success", token);
	}
	
	@Override
	public ResponseDTO verifyToken(String token) {
		try {
			GameManagerDO managerSearch = new GameManagerDO();
			managerSearch.setToken(token);
			GameManagerDO manager = gameManagerMapper.selectOne(new QueryWrapper<>(managerSearch));
			if(manager != null) {
				return new ResponseDTO(200, "token verified and succeed", null);
			}
			return new ResponseDTO(500, "token is not valid, please login first", null);
		}catch(Exception e) {
			return new ResponseDTO(501, "ERROR: token verification failed with Exception thrown", null);
		}
	}
	
	@Override
	public ResponseDTO delete(ManagerDeleteDTO dto) {
		try {
			String token = dto.getToken();
			GameManagerDO managerSearch = new GameManagerDO();
			managerSearch.setToken(token);
			// verify token
			GameManagerDO manager = gameManagerMapper.selectOne(new QueryWrapper<>(managerSearch));
			if(manager == null) {
				return new ResponseDTO(500, "token is not valid, please login first", null);
			}
			// delete manager from manager table
			String gameUuid = manager.getGameUuid();
			if (gameManagerMapper.delete(new QueryWrapper<>(managerSearch)) < 0){
				return new ResponseDTO(500, "ERROR: fail to delete manager", null);
			}
			// delete game from game table
			GameDO gameSearch = new GameDO();
			gameSearch.setUuid(gameUuid);
			if (gameMapper.delete(new QueryWrapper<>(gameSearch)) < 0) {
				return new ResponseDTO(500, "ERROR: fail to delete game", null);
			}
			// fetch and delete inventory from inventory table
			InventoryDO inventorySearch = new InventoryDO();
			inventorySearch.setGameUuid(gameUuid);
			List<InventoryDO> inventorySearchResult = inventoryMapper.selectList(new QueryWrapper<>(inventorySearch));
			if (inventoryMapper.delete(new QueryWrapper<>(inventorySearch)) < 0){
				return new ResponseDTO(500, "ERROR: fail to delete inventory", null);
			}
			// delete item from item table
			ItemDO itemSearch = new ItemDO();
			for (int i=0; i<inventorySearchResult.size(); i++){
				itemSearch.setInventoryUuid(inventorySearchResult.get(i).getUuid());
				if (itemMapper.delete(new QueryWrapper<>(itemSearch)) < 0){
					return new ResponseDTO(500, "ERROR: fail to delete item", null);
				}
			}

			return new ResponseDTO(200, "game deleted successfully", null);

		}catch(Exception e) {
			return new ResponseDTO(501, "ERROR: delete failed with Exception thrown", null);
		}
	}
	
	@Override
	public ResponseDTO info(String token) {
		GameManagerDO managerSearch = new GameManagerDO();
		managerSearch.setToken(token);
		GameManagerDO manager = gameManagerMapper.selectOne(new QueryWrapper<>(managerSearch));

		if (manager == null) {
			return new ResponseDTO(500, "token is not valid, please login first", null);
		}

		GameDO gameSearch = new GameDO();
		gameSearch.setManagerUuid(manager.getUuid());
		GameDO game = gameMapper.selectOne(new QueryWrapper<>(gameSearch));

		if (game == null) {
			return new ResponseDTO(500, "can not find gameInfo with this manager's uuid", null);
		}

		InventoryDO inventorySearch = new InventoryDO();
		inventorySearch.setGameUuid(game.getUuid());
		List<InventoryDO> inventory = inventoryMapper.selectList(new QueryWrapper<>(inventorySearch));

		if (inventory == null) {
			return new ResponseDTO(500, "can not find inventoryInfo with this game's uuid", null);
		}

		JSONObject resultJSON = new JSONObject();
		resultJSON.put("managerInfo", manager);
		resultJSON.put("gameInfo", game);
		resultJSON.put("inventoryInfo", inventory);

		return new ResponseDTO(200, "", resultJSON);
	}
	
	@Override
	public ResponseDTO logout(String token) {
		try {
			GameManagerDO managerSearch = new GameManagerDO();
			managerSearch.setToken(token);
			GameManagerDO manager = gameManagerMapper.selectOne(new QueryWrapper<>(managerSearch));

			// verify token
			if (manager == null) {
				return new ResponseDTO(500, "token is not valid, cannot logout", null);
			}
			
			// set token to null in sangria_game_manager table
			if(gameManagerMapper.updateLogout(token) <= 0) {
				return new ResponseDTO(500,"ERROR: manager logout failed", null);
			}
			return new ResponseDTO(200, "manager logout successful", null);

		} catch (Exception e) {
			return new ResponseDTO(501, "ERROR: logout failed with Exception thrown", null);
		}
	}
}
