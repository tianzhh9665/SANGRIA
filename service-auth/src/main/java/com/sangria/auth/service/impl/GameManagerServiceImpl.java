package com.sangria.auth.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sangria.auth.dao.GameManagerMapper;
import com.sangria.auth.dto.GameManagerAddDTO;
import com.sangria.auth.dto.ResponseDTO;
import com.sangria.auth.entity.GameManagerDO;
import com.sangria.auth.service.GameManagerService;
import com.sangria.auth.utils.CommonUtils;

@Service
public class GameManagerServiceImpl implements GameManagerService{
	
	@Resource
	private GameManagerMapper gameManagerMapper;
	
	
	@Override
	public int getManagerCount() {
		return gameManagerMapper.getManagerNum();
	}
	
	@Override
	public ResponseDTO addManager(GameManagerAddDTO manager) {
		GameManagerDO managerDO = CommonUtils.copyData(manager, GameManagerDO.class);
		gameManagerMapper.insert(managerDO);
		
		return new ResponseDTO(200,"",null);
	}

}
