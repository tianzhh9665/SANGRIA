package com.sangria.auth;

import java.util.Random;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangria.auth.dao.GameManagerMapper;
import com.sangria.auth.dao.GameMapper;
import com.sangria.auth.entity.GameDO;
import com.sangria.auth.entity.GameManagerDO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sangria.auth.dto.ManagerLoginDTO;
import com.sangria.auth.dto.ManagerRegDTO;
import com.sangria.auth.dto.ManagerDeleteDTO;
import com.sangria.auth.dto.ResponseDTO;
import com.sangria.auth.service.GameManagerService;
import com.sangria.auth.utils.CommonUtils;

import javax.annotation.Resource;

@SpringBootTest(classes = ServiceAuthApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ServiceAuthApplicationTests {

	@Autowired
    private GameManagerService gameManagerService;

    @Resource
    private GameManagerMapper gameManagerMapper;

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

    @Test
    void testInfo() {
        //test info success
        ManagerRegDTO dto = new ManagerRegDTO();
        String testUsername = CommonUtils.generateUniqueId("testUsername", 1);
        String testPassword = CommonUtils.generateUniqueId("testPassword", 1);
        String testGameName = CommonUtils.generateUniqueId("testGamename", 1);
        dto.setUsername(testUsername);
        dto.setPassword(testPassword);
        dto.setGameName(testGameName);
        ResponseDTO result = gameManagerService.register(dto);
        Assert.assertEquals(200, result.getCode());

        ManagerLoginDTO login = new ManagerLoginDTO();
        login.setUsername(testUsername);
        login.setPassword(testPassword);
        result = gameManagerService.login(login);
        Assert.assertEquals(200, result.getCode());

        GameManagerDO managerSearch = new GameManagerDO();
        managerSearch.setUsername(login.getUsername());
        GameManagerDO manager = gameManagerMapper.selectOne(new QueryWrapper<>(managerSearch));

        result = gameManagerService.info(manager.getToken());
        Assert.assertEquals(200, result.getCode());

        //test info fail
        GameManagerDO infoFail = new GameManagerDO();
        infoFail.setUsername("fake");
        infoFail.setPassword("fake");
        infoFail.setToken("fake");
        result = gameManagerService.info(infoFail.getToken());
        Assert.assertEquals(500, result.getCode());

    }

    @Test
    void testDelete() {
        //test delete success
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

        GameManagerDO managerSearch = new GameManagerDO();
        managerSearch.setUsername(login.getUsername());
        List<GameManagerDO> managerBeforeDelete = gameManagerMapper.selectList(new QueryWrapper<>(managerSearch));
        Assert.assertEquals(1, managerBeforeDelete.size());

        String token = managerBeforeDelete.get(0).getToken();
        ManagerDeleteDTO deleteDTO = new ManagerDeleteDTO();
        deleteDTO.setToken(token);
        result = gameManagerService.delete(deleteDTO);
        Assert.assertEquals(200, result.getCode());

        List<GameManagerDO> managerAfterDelete = gameManagerMapper.selectList(new QueryWrapper<>(managerSearch));
        Assert.assertEquals(0, managerAfterDelete.size());

        //test delete fail
        ManagerDeleteDTO deleteFail = new ManagerDeleteDTO();
        deleteFail.setToken("fake");
        result = gameManagerService.delete(deleteFail);
        Assert.assertEquals(500, result.getCode());

    }

}
