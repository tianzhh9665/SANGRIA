package com.sangria.auth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sangria.auth.dto.GameManagerAddDTO;
import com.sangria.auth.dto.ResponseDTO;
import com.sangria.auth.service.GameManagerService;

@RestController
@RequestMapping("/gameManager")
public class GameManagerController extends BaseController{
	
	@Autowired
	GameManagerService gameManagerService;
	
	@GetMapping(value = "/count")
	public ResponseDTO count() {
		
		int count = gameManagerService.getManagerCount();
		return renderOk(count);
		
	}
	
	@GetMapping(value = "/add")
	public ResponseDTO add() {
		
		String username = "fake_manager_1";
		String password = "fake_password_1";
		String fakeToken = "fakeToken";
		String fakeUuid = "fakeUuid";
		
		GameManagerAddDTO manager = new GameManagerAddDTO();
		manager.setUuid(fakeUuid);
		manager.setUsername(username);
		manager.setPassword(password);
		manager.setToken(fakeToken);
		
		ResponseDTO result = gameManagerService.addManager(manager);
		if(result != null && result.getCode() == 200) {
			return renderOk();
		}
		
		int count = gameManagerService.getManagerCount();
		return new ResponseDTO(200,"success",count);
		
	}
}
