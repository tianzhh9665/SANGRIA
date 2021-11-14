package com.sangria.auth;

import java.util.Random;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sangria.auth.dto.ManagerLoginDTO;
import com.sangria.auth.dto.ManagerRegDTO;
import com.sangria.auth.dto.ResponseDTO;
import com.sangria.auth.service.GameManagerService;
import com.sangria.auth.utils.CommonUtils;

@SpringBootTest(classes = ServiceAuthApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ServiceAuthApplicationTests {

	@Autowired
    private GameManagerService gameManagerService;
	
	@Test
	void testRegister() {
		//test register success case
		ManagerRegDTO dto = new ManagerRegDTO();
		String testUsername = CommonUtils.generateUniqueId("testUsername", 1);
		String testPassword = CommonUtils.generateUniqueId("testPassword", 1);
		String testGamename = CommonUtils.generateUniqueId("testGamename", 1);
        dto.setUsername(testUsername);
        dto.setPassword(testPassword);
        dto.setGameName(testGamename);
        ResponseDTO result = gameManagerService.register(dto);
        Assert.assertEquals(200, result.getCode());
        
        //test register fail case
        dto = new ManagerRegDTO();
        dto.setUsername(testUsername);
        dto.setPassword(testPassword);
        result = gameManagerService.register(dto);
        Assert.assertEquals(500, result.getCode());
	}
	
	@Test
	void testLogin() {
		//test login success
		ManagerRegDTO dto = new ManagerRegDTO();
		String testUsername = CommonUtils.generateUniqueId("testUsername", 1);
		String testPassword = CommonUtils.generateUniqueId("testPassword", 1);
		String testGamename = CommonUtils.generateUniqueId("testGamename", 1);
        dto.setUsername(testUsername);
        dto.setPassword(testPassword);
        dto.setGameName(testGamename);
        ResponseDTO result = gameManagerService.register(dto);
        Assert.assertEquals(200, result.getCode());
        
        ManagerLoginDTO login = new ManagerLoginDTO();
        login.setUsername(testUsername);
        login.setPassword(testPassword);
        result = gameManagerService.login(login);
        Assert.assertEquals(200, result.getCode());
        Assert.assertNotNull(result.getData());
        
        //test login fail
        ManagerLoginDTO loginFail = new ManagerLoginDTO();
        loginFail.setUsername("fake");
        loginFail.setPassword("fake");
        result = gameManagerService.login(login);
        Assert.assertEquals(500, result.getCode());
        
	}

}
