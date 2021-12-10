package com.sangria.operation;

import com.sangria.operation.dto.PlayerDeleteDTO;
import com.sangria.operation.dto.PlayerFreezeDTO;
import com.sangria.operation.dto.PlayerUnfreezeDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.service.PlayerService;
import com.sangria.operation.utils.CommonUtils;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = ServiceOperationApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ServiceOperationApplicationTests {

	@Autowired
	private PlayerService playerService;

	@Test
	void testFreeze() {
		//test success fail case


		//test freeze fail case
		PlayerFreezeDTO dto = new PlayerFreezeDTO();
		String testToken = CommonUtils.generateUniqueId("fake", 1);
		String testPlayerId = CommonUtils.generateUniqueId("fake", 1);
		dto.setToken(testToken);
		dto.setPlayerId(testPlayerId);
		ResponseDTO result = playerService.freeze(dto);
		Assert.assertEquals(500, result.getCode());

	}

	@Test
	void testUnfreeze() {
		//test unfreeze success case


		//test unfreeze fail case
		PlayerUnfreezeDTO dto = new PlayerUnfreezeDTO();
		String testToken = CommonUtils.generateUniqueId("fake", 1);
		String testPlayerId = CommonUtils.generateUniqueId("fake", 1);
		dto.setToken(testToken);
		dto.setPlayerId(testPlayerId);
		ResponseDTO result = playerService.unfreeze(dto);
		Assert.assertEquals(500, result.getCode());
	}

	@Test
	void testDeletePlayer() {
		//test deletePlayer success case


		//test deletePlayer fail case
		PlayerDeleteDTO dto = new PlayerDeleteDTO();
		String testToken = CommonUtils.generateUniqueId("fake", 1);
		String testPlayerId = CommonUtils.generateUniqueId("fake", 1);
		dto.setToken(testToken);
		dto.setPlayerId(testPlayerId);
		ResponseDTO result = playerService.deletePlayer(dto);
		Assert.assertEquals(500, result.getCode());
	}
}
