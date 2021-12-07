package com.sangria.operation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sangria.operation.Enum.PlayerTradeTypeEnum;
import com.sangria.operation.dao.GameManagerMapper;
import com.sangria.operation.dto.ItemMakeDTO;
import com.sangria.operation.dto.PlayerAddDTO;
import com.sangria.operation.dto.PlayerTradeRequestDTO;
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
	
	String testUsername = "tianzhh";
	String testInventory = "INV20211205224845599";

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
