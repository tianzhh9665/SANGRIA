package com.sangria.auth.service.impl;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangria.auth.dao.GameManagerMapper;
import com.sangria.auth.dao.GameMapper;
import com.sangria.auth.dto.ManagerLoginDTO;
import com.sangria.auth.dto.ManagerRegDTO;
import com.sangria.auth.dto.ResponseDTO;
import com.sangria.auth.entity.GameDO;
import com.sangria.auth.entity.GameManagerDO;
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
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseDTO register(ManagerRegDTO dto) {
		
		GameManagerDO managerSearch = new GameManagerDO();
		managerSearch.setUsername(dto.getUsername());
		List<GameManagerDO> managerSearchResult = gameManagerMapper.selectList(new QueryWrapper<>(managerSearch));
		if(managerSearchResult != null && managerSearchResult.size() >= 1) {
			return new ResponseDTO(500, "ERROR: username already exists, please choose another one", null);
		}
		
		
		String gameName = dto.getGameName();
		GameDO gameSearch = new GameDO();
		gameSearch.setName(gameName);
		List<GameDO> gameSearchResult = gameMapper.selectList(new QueryWrapper<>(gameSearch));
		if(gameSearchResult != null && gameSearchResult.size() >= 1) {
			return new ResponseDTO(500, "ERROR: The game you want to register already had a manager", null);
		}
		
		
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
		GameManagerDO managerSearch = new GameManagerDO();
		managerSearch.setUsername(dto.getUsername());
		List<GameManagerDO> managerSearchResult = gameManagerMapper.selectList(new QueryWrapper<>(managerSearch));
		if(managerSearchResult == null || managerSearchResult.size() == 0) {
			return new ResponseDTO(500, "ERROR: manager with username: " + dto.getUsername() + " does not exists, please register first", null);
		}
		if(managerSearchResult != null && managerSearchResult.size() > 1) {
			return new ResponseDTO(500, "ERROR: more that one manager with username: " + dto.getUsername() + " , please contact system admin", null);
		}
		
		GameManagerDO manager = managerSearchResult.get(0);
		if(StringUtils.isNotEmpty(manager.getToken())) {
			return new ResponseDTO(500, "ERROR: manager " + dto.getUsername() + " already logged in", null);
		}
		
		String encryptedPass = MD5Utils.getMD5(dto.getPassword());
		if(!encryptedPass.equals(manager.getPassword())) {
			return new ResponseDTO(500, "ERROR: incorrect password, please try again", null);
		}
		String token = UUID.randomUUID().toString().replace("-","");
		manager.setToken(token);
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
}
