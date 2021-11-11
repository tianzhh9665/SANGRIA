package com.sangria.auth.service;

import com.sangria.auth.dto.GameManagerAddDTO;
import com.sangria.auth.dto.ResponseDTO;

public interface GameManagerService {
	
	int getManagerCount();
	
	ResponseDTO addManager(GameManagerAddDTO manager);
}
