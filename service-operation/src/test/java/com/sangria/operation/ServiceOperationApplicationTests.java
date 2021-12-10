package com.sangria.operation;

import java.util.ArrayList;
import java.util.List;

import com.sangria.operation.Enum.PlayerStatusEnum;
import com.sangria.operation.dao.PlayerMapper;
import com.sangria.operation.dto.*;
import com.sangria.operation.entity.PlayerDO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangria.operation.Enum.PlayerTradeTypeEnum;
import com.sangria.operation.dao.GameManagerMapper;
import com.sangria.operation.entity.GameManagerDO;
import com.sangria.operation.service.InventoryService;
import com.sangria.operation.service.ItemService;
import com.sangria.operation.service.PlayerService;

@SpringBootTest(classes = ServiceOperationApplication.class,
webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ServiceOperationApplicationTests {
	
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private GameManagerMapper gameManagerMapper;

	@Autowired
	private PlayerMapper playerMapper;
	
	String testUsername = "tianzhh";
	String testInventory = "INV20211205224845599";
	String testFreezePlayerId = "testFreeze";
	String testUnfreezePlayerId = "testUnfreeze";
	String testDeletePlayerId = "testDelete";
	String testManagerId = "testManager";

	@Test
	void testFreeze() {
		// test success freeze
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		PlayerFreezeDTO freezeDTO = new PlayerFreezeDTO();
		freezeDTO.setToken(testToken);
		freezeDTO.setPlayerId(testFreezePlayerId);

		PlayerDO player = new PlayerDO();
		player.setUuid(testFreezePlayerId);
		String status = playerMapper.selectOne(new QueryWrapper<>(player)).getStatus();

		if (status.equals(PlayerStatusEnum.FROZEN.getStatus())) {
			player.setStatus(PlayerStatusEnum.NORMAL.getStatus());
			playerMapper.updateById(player);
		}

		Assert.assertEquals(200, playerService.freeze(freezeDTO).getCode());

		// test invalid token
		freezeDTO = new PlayerFreezeDTO();
		freezeDTO.setToken("fakeToken");
		freezeDTO.setPlayerId(testFreezePlayerId);
		Assert.assertEquals(500, playerService.freeze(freezeDTO).getCode());

		// test invalid playerId
		freezeDTO = new PlayerFreezeDTO();
		freezeDTO.setToken(testToken);
		freezeDTO.setPlayerId("fakePlayerId");
		Assert.assertEquals(500, playerService.freeze(freezeDTO).getCode());

		// test freeze player who has been already freezed
		freezeDTO = new PlayerFreezeDTO();
		freezeDTO.setToken(testToken);
		freezeDTO.setPlayerId(testFreezePlayerId);
		Assert.assertEquals(500, playerService.freeze(freezeDTO).getCode());
	}

	@Test
	void testUnfreeze() {
		// test success unfreeze
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		PlayerUnfreezeDTO unfreezeDTO = new PlayerUnfreezeDTO();
		unfreezeDTO.setToken(testToken);
		unfreezeDTO.setPlayerId(testUnfreezePlayerId);

		PlayerDO player = new PlayerDO();
		player.setUuid(testUnfreezePlayerId);
		String status = playerMapper.selectOne(new QueryWrapper<>(player)).getStatus();

		if (status.equals(PlayerStatusEnum.NORMAL.getStatus())) {
			player.setStatus(PlayerStatusEnum.FROZEN.getStatus());
			playerMapper.updateById(player);
		}

		Assert.assertEquals(200, playerService.unfreeze(unfreezeDTO).getCode());

		// test invalid token
		unfreezeDTO = new PlayerUnfreezeDTO();
		unfreezeDTO.setToken("fakeToken");
		unfreezeDTO.setPlayerId(testFreezePlayerId);
		Assert.assertEquals(500, playerService.unfreeze(unfreezeDTO).getCode());

		// test invalid playerId
		unfreezeDTO = new PlayerUnfreezeDTO();
		unfreezeDTO.setToken(testToken);
		unfreezeDTO.setPlayerId("fakePlayerId");
		Assert.assertEquals(500, playerService.unfreeze(unfreezeDTO).getCode());

		// test freeze player who has been already freezed
		unfreezeDTO = new PlayerUnfreezeDTO();
		unfreezeDTO.setToken(testToken);
		unfreezeDTO.setPlayerId(testUnfreezePlayerId);
		Assert.assertEquals(500, playerService.unfreeze(unfreezeDTO).getCode());
	}

	@Test
	void testDeletePlayer() {
		// test success delete
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		PlayerDeleteDTO deleteDTO = new PlayerDeleteDTO();
		deleteDTO.setToken(testToken);
		deleteDTO.setPlayerId(testDeletePlayerId);

		PlayerDO player = new PlayerDO();
		player.setUuid(testDeletePlayerId);
		String gameUuid = playerMapper.selectOne(new QueryWrapper<>(player)).getGameUuid();
		String inventoryUuid = playerMapper.selectOne(new QueryWrapper<>(player)).getGameInventoryUuid();
		int balance = playerMapper.selectOne(new QueryWrapper<>(player)).getBalance();
		String status = playerMapper.selectOne(new QueryWrapper<>(player)).getStatus();

		Assert.assertEquals(200, playerService.deletePlayer(deleteDTO).getCode());

		player = new PlayerDO();
		player.setUuid(testDeletePlayerId);
		player.setGameUuid(gameUuid);
		player.setGameInventoryUuid(inventoryUuid);
		player.setBalance(balance);
		player.setStatus(status);

		playerMapper.insert(player);

		// test invalid token
		deleteDTO = new PlayerDeleteDTO();
		deleteDTO.setPlayerId(testDeletePlayerId);
		deleteDTO.setToken("fakeToken");
		Assert.assertEquals(500, playerService.deletePlayer(deleteDTO).getCode());

		// test manager does not have game
		manager = new GameManagerDO();
		manager.setUuid(testManagerId);
		String token = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		deleteDTO = new PlayerDeleteDTO();
		deleteDTO.setToken(token);
		deleteDTO.setPlayerId("no game");
		Assert.assertEquals(500, playerService.deletePlayer(deleteDTO).getCode());

	}

	@Test
	void testPlayerAdd() {
		//test add player ok
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		
		PlayerAddDTO addDTO = new PlayerAddDTO();
		addDTO.setToken(testToken);
		addDTO.setInventoryUuid(testInventory);
		
		Assert.assertEquals(200, playerService.add(addDTO).getCode());
		
		//test invalid token
		addDTO = new PlayerAddDTO();
		addDTO.setToken("fakeToken");
		addDTO.setInventoryUuid(testInventory);
		
		Assert.assertEquals(500, playerService.add(addDTO).getCode());
		
		//test invalid inventoryId
		addDTO = new PlayerAddDTO();
		addDTO.setToken(testToken);
		addDTO.setInventoryUuid("none-existing-inventoryid");
		
		Assert.assertEquals(500, playerService.add(addDTO).getCode());
	}
	
	@Test
	void testPlayerInfo() {
		
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		String playerUuid = "PLAYER2021120602362410108";
		
		Assert.assertEquals(200, playerService.info(playerUuid, testToken).getCode());
		Assert.assertEquals(500, playerService.info(playerUuid, "fakeToken").getCode());
		Assert.assertEquals(500, playerService.info("fake-player-uuid", testToken).getCode());
	}
	
	@Test
	void testMakeItem() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		String playerUuid = "PLAYER2021120523161171613";
		String itemUuid = "ITEM2021120522523214154";
		int amount = 1;
		
		ItemMakeDTO makeItemDTO = new ItemMakeDTO();
		makeItemDTO.setToken(testToken);
		makeItemDTO.setAmount(amount);
		makeItemDTO.setPlayerUuid(playerUuid);
		makeItemDTO.setItemUuid(itemUuid);
		
		Assert.assertEquals(200, itemService.make(makeItemDTO).getCode());
		
		itemUuid = "ITEM2021120522494661391";
		makeItemDTO.setItemUuid(itemUuid);
		Assert.assertEquals(500, itemService.make(makeItemDTO).getCode());
	}
	
	@Test
	void testPlayerTrade() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		String player1Uuid = "PLAYER2021120523161171613";
		String player2Uuid = "PLAYER2021120602362410108";
		
		PlayerTradeRequestDTO tradeDTO = new PlayerTradeRequestDTO();
		tradeDTO.setToken(testToken);
		tradeDTO.setPlayer1Uuid(player1Uuid);
		tradeDTO.setPlayer2Uuid(player2Uuid);
		tradeDTO.setType(PlayerTradeTypeEnum.MONEY_TO_MONEY.getType());
		tradeDTO.setPlayer1Amount(1);
		tradeDTO.setPlayer2Amount(2);
		
		Assert.assertEquals(200, playerService.trade(tradeDTO).getCode());
		tradeDTO.setPlayer2Amount(100000);
		Assert.assertEquals(500, playerService.trade(tradeDTO).getCode());
		
		tradeDTO.setType(PlayerTradeTypeEnum.MONEY_TO_ITEM.getType());
		tradeDTO.setPlayer1Amount(1);
		List<List> itemList = new ArrayList<>();
		List<Object> items = new ArrayList<>();
		
		items.add("ITEM2021120522492419219");
		items.add(1);
		itemList.add(items);
		tradeDTO.setPlayer2ItemList(itemList);
		Assert.assertEquals(200, playerService.trade(tradeDTO).getCode());
		
		itemList = new ArrayList<>();
		items = new ArrayList<>();
		items.add("ITEM2021120522492419219");
		items.add(10000);
		itemList.add(items);
		tradeDTO.setPlayer2ItemList(itemList);
		Assert.assertEquals(500, playerService.trade(tradeDTO).getCode());
		
		tradeDTO.setType(PlayerTradeTypeEnum.ITEM_TO_MONEY.getType());
		tradeDTO.setPlayer2Amount(1);
		itemList = new ArrayList<>();
		items = new ArrayList<>();
		items.add("ITEM2021120522492419219");
		items.add(1);
		itemList.add(items);
		tradeDTO.setPlayer1ItemList(itemList);
		
		Assert.assertEquals(200, playerService.trade(tradeDTO).getCode());
		itemList = new ArrayList<>();
		items = new ArrayList<>();
		items.add("ITEM2021120522492419219");
		items.add(1000000);
		itemList.add(items);
		tradeDTO.setPlayer1ItemList(itemList);
		
		Assert.assertEquals(500, playerService.trade(tradeDTO).getCode());
		
		tradeDTO.setType(PlayerTradeTypeEnum.ITEM_TO_ITEM.getType());
		itemList = new ArrayList<>();
		items = new ArrayList<>();
		items.add("ITEM2021120522492419219");
		items.add(1);
		itemList.add(items);
		tradeDTO.setPlayer1ItemList(itemList);
		tradeDTO.setPlayer2ItemList(itemList);
		
		Assert.assertEquals(200, playerService.trade(tradeDTO).getCode());
		itemList = new ArrayList<>();
		items = new ArrayList<>();
		items.add("ITEM2021120522492419219");
		items.add(1000000);
		itemList.add(items);
		tradeDTO.setPlayer1ItemList(itemList);
		tradeDTO.setPlayer2ItemList(itemList);
		
		Assert.assertEquals(500, playerService.trade(tradeDTO).getCode());
	}

}
