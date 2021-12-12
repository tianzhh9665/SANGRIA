package com.sangria.auth.service;

import com.sangria.auth.dto.ManagerDeleteDTO;
import com.sangria.auth.dto.ManagerLoginDTO;
import com.sangria.auth.dto.ManagerRegDTO;
import com.sangria.auth.dto.ResponseDTO;

public interface GameManagerService {
	
	ResponseDTO register(ManagerRegDTO dto);
	
	ResponseDTO login(ManagerLoginDTO dto);
	
	//ResponseDTO verifyToken(String token);
	
	ResponseDTO delete(ManagerDeleteDTO dto);
	
	ResponseDTO info(String token);
	
	ResponseDTO logout(String token);

}
