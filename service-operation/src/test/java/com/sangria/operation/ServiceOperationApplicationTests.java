package com.sangria.operation;

import java.util.ArrayList;
import java.util.List;

import com.sangria.operation.Enum.ItemTypeEnum;
import com.sangria.operation.Enum.PlayerStatusEnum;
import com.sangria.operation.dao.ItemMapper;
import com.sangria.operation.dao.PlayerInventoryMapper;
import com.sangria.operation.dao.PlayerMapper;
import com.sangria.operation.dto.*;
import com.sangria.operation.entity.ItemDO;
import com.sangria.operation.entity.PlayerDO;
import com.sangria.operation.entity.PlayerInventoryDO;
import com.sangria.operation.utils.CommonUtils;
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

import javax.annotation.Resource;

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

	@Resource
	private PlayerInventoryMapper playerInventoryMapper;

	@Resource
	private ItemMapper itemMapper;

	String testUsername = "tianzhh";
	String testInventory = "INV20211205224845599";
	String testFreezePlayerId = "testFreeze";
	String testUnfreezePlayerId = "testUnfreeze";
	String testDeletePlayerId = "testDelete";
	String testManagerId = "testManager";
	String testPlayerUuid = "PLAYER2021120523161171613";
	String testItemUuid = "ITEM2021120522492419219";
	String testItemUuidAnotherInventory = "ITEM2021120810532211189";

	@Test
	void testFreeze() {
		// test success freeze
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		PlayerFreezeDTO freezeDTO = new PlayerFreezeDTO();
		freezeDTO.setToken(testToken);
		freezeDTO.setPlayerId(testFreezePlayerId);

		PlayerDO playerSearch = new PlayerDO();
		playerSearch.setUuid(testFreezePlayerId);
		PlayerDO player = playerMapper.selectOne(new QueryWrapper<>(playerSearch));
		String status = player.getStatus();

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

		PlayerDO playerSearch = new PlayerDO();
		playerSearch.setUuid(testUnfreezePlayerId);
		PlayerDO player = playerMapper.selectOne(new QueryWrapper<>(playerSearch));
		String status = player.getStatus();

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

	@Test
	void testPlayerRemoveMoney() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String playerUuid = testPlayerUuid;
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		Integer amount = 100;
		Integer testBalance = 1000;

		// record player balance and status information, and replace it with test values
		PlayerDO playerSearchDO = new PlayerDO();
		playerSearchDO.setUuid(playerUuid);
		PlayerDO playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);

		String playerStatus = playerDO.getStatus();
		Integer balance = playerDO.getBalance();
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		playerDO.setBalance(testBalance);
		Integer code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player balance and status", code >= 0);

		// initialize removeMoneyDTO
		PlayerRemoveMoneyDTO removeMoneyDTO = new PlayerRemoveMoneyDTO();
		removeMoneyDTO.setToken(testToken);
		removeMoneyDTO.setPlayerUuid(playerUuid);
		removeMoneyDTO.setAmount(amount);

		// invalid token
		String testTokenInvalid = "invalidToken";
		removeMoneyDTO.setToken(testTokenInvalid);
		Assert.assertEquals(500, playerService.removeMoney(removeMoneyDTO).getCode());
		removeMoneyDTO.setToken(testToken);

		// non-existing player
		String playerUuidInvalid = "PLAYER2021120523161171612";
		removeMoneyDTO.setPlayerUuid(playerUuidInvalid);
		Assert.assertEquals(500, playerService.removeMoney(removeMoneyDTO).getCode());
		removeMoneyDTO.setPlayerUuid(playerUuid);

		// frozen player
		playerDO.setStatus(PlayerStatusEnum.FROZEN.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to frozen", code >= 0);
		Assert.assertEquals(500, playerService.removeMoney(removeMoneyDTO).getCode());
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to normal", code >= 0);

		// insufficient money
		removeMoneyDTO.setAmount(testBalance+1);
		Assert.assertEquals(500, playerService.removeMoney(removeMoneyDTO).getCode());
		removeMoneyDTO.setAmount(amount);

		// happy path
		Assert.assertEquals(200, playerService.removeMoney(removeMoneyDTO).getCode());

		// check the amount after removal
		playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);
		Assert.assertTrue(playerDO.getBalance() == testBalance-amount);

		// restore player information before test
		playerDO.setStatus(playerStatus);
		playerDO.setBalance(balance);
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: restore player balance and status", code >= 0);

	}

	@Test
	void testPlayerBuyItemSys() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String playerUuid = testPlayerUuid;
		String itemUuid = testItemUuid;
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		// get item price
		ItemDO itemSearch = new ItemDO();
		itemSearch.setUuid(itemUuid);
		ItemDO item = itemMapper.selectOne(new QueryWrapper<>(itemSearch));
		Assert.assertNotEquals(item, null);
		Integer price = item.getPrice();
		Assert.assertTrue("item price must be positive", price > 0);

		Integer itemAmount = 10;
		Integer itemAmountTooMany = 30;
		Integer testBalance = price*20;

		// record player balance and status information, and replace it with test values
		PlayerDO playerSearchDO = new PlayerDO();
		playerSearchDO.setUuid(playerUuid);
		PlayerDO playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);

		String oldPlayerStatus = playerDO.getStatus();
		Integer oldBalance = playerDO.getBalance();
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		playerDO.setBalance(testBalance);
		Integer code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player balance and status", code >= 0);

		// initialize buyItemSysDTO
		PlayerBuyItemSysDTO buyItemSysDTO = new PlayerBuyItemSysDTO();
		buyItemSysDTO.setToken(testToken);
		buyItemSysDTO.setPlayerUuid(playerUuid);
		buyItemSysDTO.setAmount(itemAmount);
		buyItemSysDTO.setItemUuid(itemUuid);

		// invalid token
		String testTokenInvalid = "invalidToken";
		buyItemSysDTO.setToken(testTokenInvalid);
		Assert.assertEquals(500, playerService.buyItemSys(buyItemSysDTO).getCode());
		buyItemSysDTO.setToken(testToken);

		// non-existing item
		String itemUuidInvalid = "invalidItem";
		buyItemSysDTO.setItemUuid(itemUuidInvalid);
		Assert.assertEquals(500, playerService.buyItemSys(buyItemSysDTO).getCode());
		buyItemSysDTO.setItemUuid(itemUuid);

		// non-existing player
		String playerUuidInvalid = "PLAYER2021120523161171612";
		buyItemSysDTO.setPlayerUuid(playerUuidInvalid);
		Assert.assertEquals(500, playerService.buyItemSys(buyItemSysDTO).getCode());
		buyItemSysDTO.setPlayerUuid(playerUuid);

		// frozen player
		playerDO.setStatus(PlayerStatusEnum.FROZEN.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to frozen", code >= 0);
		Assert.assertEquals(500, playerService.buyItemSys(buyItemSysDTO).getCode());
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to normal", code >= 0);

		// item from another inventory of the same game
		String anotherItemUuid = testItemUuidAnotherInventory;
		buyItemSysDTO.setItemUuid(anotherItemUuid);
		Assert.assertEquals(500, playerService.buyItemSys(buyItemSysDTO).getCode());
		buyItemSysDTO.setItemUuid(itemUuid);

		// insufficient money
		buyItemSysDTO.setAmount(itemAmountTooMany);
		Assert.assertEquals(500, playerService.buyItemSys(buyItemSysDTO).getCode());
		buyItemSysDTO.setAmount(itemAmount);

		// record player inventory information
		PlayerInventoryDO playerInventorySearch = new PlayerInventoryDO();
		playerInventorySearch.setPlayerUuid(playerUuid);
		playerInventorySearch.setItemUuid(itemUuid);
		PlayerInventoryDO oldPlayerInventory = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		if (oldPlayerInventory != null) {
			playerInventoryMapper.deleteById(oldPlayerInventory);
		}

		// player does not have any item yet
		Assert.assertEquals(200, playerService.buyItemSys(buyItemSysDTO).getCode());
		PlayerInventoryDO playerInventoryDO = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		Assert.assertNotEquals(playerInventoryDO, null);
		Assert.assertTrue(playerInventoryDO.getAmount() == itemAmount);

		playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);
		Assert.assertTrue(playerDO.getBalance() == testBalance-itemAmount*price);

		// player already have some amount of this item
		Assert.assertEquals(200, playerService.buyItemSys(buyItemSysDTO).getCode());
		playerInventoryDO = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		Assert.assertNotEquals(playerInventoryDO, null);
		Assert.assertTrue(playerInventoryDO.getAmount() == 2*itemAmount);

		playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);
		Assert.assertTrue(playerDO.getBalance() == testBalance-2*itemAmount*price);

		// restore player inventory information
		playerInventoryMapper.deleteById(playerInventoryDO);
		if (oldPlayerInventory != null) {
			playerInventoryMapper.insert(oldPlayerInventory);
		}

		// restore player information before test
		playerDO.setStatus(oldPlayerStatus);
		playerDO.setBalance(oldBalance);
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: restore player balance and status", code >= 0);

	}

	@Test
	void testPlayerSellItemSys() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String playerUuid = testPlayerUuid;
		String itemUuid = testItemUuid;
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		// get item price
		ItemDO itemSearch = new ItemDO();
		itemSearch.setUuid(itemUuid);
		ItemDO item = itemMapper.selectOne(new QueryWrapper<>(itemSearch));
		Assert.assertNotEquals(item, null);
		Integer price = item.getPrice();
		Assert.assertTrue("item price must be positive", price > 0);

		Integer itemAmount = 20;
		Integer sellAmount = 10;
		Integer sellAmountTooMany = 30;

		// record player balance and status information, and replace it with test values
		PlayerDO playerSearchDO = new PlayerDO();
		playerSearchDO.setUuid(playerUuid);
		PlayerDO playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertTrue(playerDO != null);

		String oldPlayerStatus = playerDO.getStatus();
		Integer oldBalance = playerDO.getBalance();
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		Integer code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: initialize player status to normal", code >= 0);

		// initialize buyItemSysDTO
		PlayerSellItemSysDTO sellItemSysDTO = new PlayerSellItemSysDTO();
		sellItemSysDTO.setToken(testToken);
		sellItemSysDTO.setPlayerUuid(playerUuid);
		sellItemSysDTO.setAmount(sellAmount);
		sellItemSysDTO.setItemUuid(itemUuid);

		// invalid token
		String testTokenInvalid = "invalidToken";
		sellItemSysDTO.setToken(testTokenInvalid);
		Assert.assertEquals(500, playerService.sellItemSys(sellItemSysDTO).getCode());
		sellItemSysDTO.setToken(testToken);

		// non-existing item
		String itemUuidInvalid = "invalidItem";
		sellItemSysDTO.setItemUuid(itemUuidInvalid);
		Assert.assertEquals(500, playerService.sellItemSys(sellItemSysDTO).getCode());
		sellItemSysDTO.setItemUuid(itemUuid);

		// non-existing player
		String playerUuidInvalid = "PLAYER2021120523161171612";
		sellItemSysDTO.setPlayerUuid(playerUuidInvalid);
		Assert.assertEquals(500, playerService.sellItemSys(sellItemSysDTO).getCode());
		sellItemSysDTO.setPlayerUuid(playerUuid);

		// frozen player
		playerDO.setStatus(PlayerStatusEnum.FROZEN.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to frozen", code >= 0);
		Assert.assertEquals(500, playerService.sellItemSys(sellItemSysDTO).getCode());
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to normal", code >= 0);

		// item from another inventory of the same game
		String anotherItemUuid = testItemUuidAnotherInventory;
		sellItemSysDTO.setItemUuid(anotherItemUuid);
		Assert.assertEquals(500, playerService.sellItemSys(sellItemSysDTO).getCode());
		sellItemSysDTO.setItemUuid(itemUuid);

		// record player inventory information
		PlayerInventoryDO playerInventorySearch = new PlayerInventoryDO();
		playerInventorySearch.setPlayerUuid(playerUuid);
		playerInventorySearch.setItemUuid(itemUuid);
		PlayerInventoryDO oldPlayerInventory = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		if (oldPlayerInventory != null) {
			playerInventoryMapper.deleteById(oldPlayerInventory);
		}

		PlayerInventoryDO playerInventory = new PlayerInventoryDO();
		playerInventory.setPlayerUuid(playerUuid);
		playerInventory.setItemUuid(itemUuid);
		playerInventory.setUuid(CommonUtils.generateUniqueId("PINV", 3));
		playerInventory.setCreateTime(CommonUtils.getTimeNow());
		playerInventory.setModifiedTime(CommonUtils.getTimeNow());
		playerInventory.setAmount(itemAmount);
		code = playerInventoryMapper.insert(playerInventory);
		Assert.assertTrue(code > 0);

		// sell amount exceeds item amount
		sellItemSysDTO.setAmount(sellAmountTooMany);
		Assert.assertEquals(500, playerService.sellItemSys(sellItemSysDTO).getCode());
		sellItemSysDTO.setAmount(sellAmount);

		// player sell some items
		Assert.assertEquals(200, playerService.sellItemSys(sellItemSysDTO).getCode());
		PlayerInventoryDO playerInventoryDO = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		Assert.assertTrue(playerInventoryDO != null);
		Assert.assertTrue(playerInventoryDO.getAmount() == itemAmount-sellAmount);

		playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertTrue(playerDO != null);
		Assert.assertTrue(playerDO.getBalance() == oldBalance+price*sellAmount);

		// player sell all items
		Assert.assertEquals(200, playerService.sellItemSys(sellItemSysDTO).getCode());
		playerInventoryDO = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		Assert.assertTrue(playerInventoryDO == null);

		playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertTrue(playerDO != null);
		Assert.assertTrue(playerDO.getBalance() == oldBalance+2*price*sellAmount);

		// restore player inventory information
		playerInventoryMapper.deleteById(playerInventoryDO);
		if (oldPlayerInventory != null) {
			playerInventoryMapper.insert(oldPlayerInventory);
		}

		// restore player information before test
		playerDO.setStatus(oldPlayerStatus);
		playerDO.setBalance(oldBalance);
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: restore player balance and status", code >= 0);
	}
	
	

	@Test
	void testMoneyAdd() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String playerUuid = testPlayerUuid;
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		Integer amount = 100;
		Integer testBalance = 1000;

		// record player balance and status information, and replace it with test values
		PlayerDO playerSearchDO = new PlayerDO();
		playerSearchDO.setUuid(playerUuid);
		PlayerDO playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);

		String playerStatus = playerDO.getStatus();
		Integer balance = playerDO.getBalance();
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		playerDO.setBalance(testBalance);
		Integer code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player balance and status", code >= 0);

		// initialize addMoneyDTO
		PlayerAddMoneyDTO addMoneyDTO = new PlayerAddMoneyDTO();
		addMoneyDTO.setToken(testToken);
		addMoneyDTO.setPlayerUuid(playerUuid);
		addMoneyDTO.setAmount(amount);

		// invalid token
		String testTokenInvalid = "fakeeeeeeeeeeeeeeeeeeeeeee";
		addMoneyDTO.setToken(testTokenInvalid);
		Assert.assertEquals(500, playerService.addMoney(addMoneyDTO).getCode());
		addMoneyDTO.setToken(testToken);

		// non-existing player
		String playerUuidInvalid = "fakekekekekekkk";
		addMoneyDTO.setPlayerUuid(playerUuidInvalid);
		Assert.assertEquals(500, playerService.addMoney(addMoneyDTO).getCode());
		addMoneyDTO.setPlayerUuid(playerUuid);

		// frozen player
		playerDO.setStatus(PlayerStatusEnum.FROZEN.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to frozen", code >= 0);
		Assert.assertEquals(500, playerService.addMoney(addMoneyDTO).getCode());
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to normal", code >= 0);

		// happy path
		Assert.assertEquals(200, playerService.addMoney(addMoneyDTO).getCode());

		// check the amount after adding
		playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);
		Assert.assertTrue(playerDO.getBalance() == testBalance+amount);

		// restore player information before test
		playerDO.setStatus(playerStatus);
		playerDO.setBalance(balance);
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: restore player balance and status", code >= 0);

	}

	@Test
	void testItemAdd() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String playerUuid = testPlayerUuid;
		String itemUuid = testItemUuid;
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		// item search
		ItemDO itemSearch = new ItemDO();
		itemSearch.setUuid(itemUuid);
		ItemDO item = itemMapper.selectOne(new QueryWrapper<>(itemSearch));
		Assert.assertNotEquals(item, null);

		Integer itemAmount = 10;

		// player search
		PlayerDO playerSearchDO = new PlayerDO();
		playerSearchDO.setUuid(playerUuid);
		PlayerDO playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);

		// initialize PlayerItemDTO
		PlayerItemDTO playerItemDTO = new PlayerItemDTO();
		playerItemDTO.setToken(testToken);
		playerItemDTO.setPlayerUuid(playerUuid);
		playerItemDTO.setAmount(itemAmount);
		playerItemDTO.setItemUuid(itemUuid);

		// invalid token
		String testTokenInvalid = "thismustnotberightwhatsoever";
		playerItemDTO.setToken(testTokenInvalid);
		Assert.assertEquals(500, playerService.addItem(playerItemDTO).getCode());
		playerItemDTO.setToken(testToken);

		// non-existing player
		String playerUuidInvalid = "andIdoNoTExiStttTtTT";
		playerItemDTO.setPlayerUuid(playerUuidInvalid);
		Assert.assertEquals(500, playerService.addItem(playerItemDTO).getCode());
		playerItemDTO.setPlayerUuid(playerUuid);

		// frozen player
		playerDO.setStatus(PlayerStatusEnum.FROZEN.getStatus());
		Integer code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to frozen", code >= 0);
		Assert.assertEquals(500, playerService.addItem(playerItemDTO).getCode());
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to normal", code >= 0);
		
		// non-existing item
		String itemUuidInvalid = "elixirthatfixeseverythingjustsimplydoesnotexist40004";
		playerItemDTO.setItemUuid(itemUuidInvalid);
		Assert.assertEquals(500, playerService.addItem(playerItemDTO).getCode());
		playerItemDTO.setItemUuid(itemUuid);
		
		// item from another inventory of the same game
		String anotherItemUuid = testItemUuidAnotherInventory;
		playerItemDTO.setItemUuid(anotherItemUuid);
		Assert.assertEquals(500, playerService.addItem(playerItemDTO).getCode());
		playerItemDTO.setItemUuid(itemUuid);

		// record player inventory information
		PlayerInventoryDO playerInventorySearch = new PlayerInventoryDO();
		playerInventorySearch.setPlayerUuid(playerUuid);
		playerInventorySearch.setItemUuid(itemUuid);
		PlayerInventoryDO oldPlayerInventory = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		if (oldPlayerInventory != null) {
			playerInventoryMapper.deleteById(oldPlayerInventory);
		}

		// the item is not already in the inventory, need to create first
		Assert.assertEquals(200, playerService.addItem(playerItemDTO).getCode());
		PlayerInventoryDO playerInventoryDO = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		Assert.assertNotEquals(playerInventoryDO, null);
		Assert.assertTrue(playerInventoryDO.getAmount() == itemAmount);

		// player already have the item
		Assert.assertEquals(200, playerService.addItem(playerItemDTO).getCode());
		playerInventoryDO = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		Assert.assertNotEquals(playerInventoryDO, null);
		Assert.assertTrue(playerInventoryDO.getAmount() == 2*itemAmount);

		// restore player inventory information
		playerInventoryMapper.deleteById(playerInventoryDO);
		if (oldPlayerInventory != null) {
			playerInventoryMapper.insert(oldPlayerInventory);
		}
	}

	@Test
	void testItemRemove() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String playerUuid = testPlayerUuid;
		String itemUuid = testItemUuid;
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();

		// item search
		ItemDO itemSearch = new ItemDO();
		itemSearch.setUuid(itemUuid);
		ItemDO item = itemMapper.selectOne(new QueryWrapper<>(itemSearch));
		Assert.assertNotEquals(item, null);

		Integer itemAmount = 10;
		Integer itemStartWith = 100;
		Integer itemTooMany = 1000;

		// player search
		PlayerDO playerSearchDO = new PlayerDO();
		playerSearchDO.setUuid(playerUuid);
		PlayerDO playerDO = playerMapper.selectOne(new QueryWrapper<>(playerSearchDO));
		Assert.assertNotEquals(playerDO, null);

		// initialize PlayerItemDTO
		PlayerItemDTO playerItemDTO = new PlayerItemDTO();
		playerItemDTO.setToken(testToken);
		playerItemDTO.setPlayerUuid(playerUuid);
		playerItemDTO.setAmount(itemAmount);
		playerItemDTO.setItemUuid(itemUuid);

		// invalid token
		String testTokenInvalid = "thismustnotberightwhatsoever";
		playerItemDTO.setToken(testTokenInvalid);
		Assert.assertEquals(500, playerService.removeItem(playerItemDTO).getCode());
		playerItemDTO.setToken(testToken);

		// non-existing player
		String playerUuidInvalid = "andIdoNoTExiStttTtTT";
		playerItemDTO.setPlayerUuid(playerUuidInvalid);
		Assert.assertEquals(500, playerService.removeItem(playerItemDTO).getCode());
		playerItemDTO.setPlayerUuid(playerUuid);

		// frozen player
		playerDO.setStatus(PlayerStatusEnum.FROZEN.getStatus());
		Integer code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to frozen", code >= 0);
		Assert.assertEquals(500, playerService.removeItem(playerItemDTO).getCode());
		playerDO.setStatus(PlayerStatusEnum.NORMAL.getStatus());
		code = playerMapper.updateById(playerDO);
		Assert.assertTrue("database operation: change player status to normal", code >= 0);
		
		// non-existing item
		String itemUuidInvalid = "elixirthatfixeseverythingjustsimplydoesnotexist40004";
		playerItemDTO.setItemUuid(itemUuidInvalid);
		Assert.assertEquals(500, playerService.removeItem(playerItemDTO).getCode());
		playerItemDTO.setItemUuid(itemUuid);
		
		// item from another inventory of the same game
		String anotherItemUuid = testItemUuidAnotherInventory;
		playerItemDTO.setItemUuid(anotherItemUuid);
		Assert.assertEquals(500, playerService.addItem(playerItemDTO).getCode());
		playerItemDTO.setItemUuid(itemUuid);

		// record player inventory information
		PlayerInventoryDO playerInventorySearch = new PlayerInventoryDO();
		playerInventorySearch.setPlayerUuid(playerUuid);
		playerInventorySearch.setItemUuid(itemUuid);
		PlayerInventoryDO oldPlayerInventory = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		if (oldPlayerInventory != null) {
			playerInventoryMapper.deleteById(oldPlayerInventory);
		}

		// the player does not have the item, nothing to delete
		Assert.assertEquals(500, playerService.removeItem(playerItemDTO).getCode());

		// player have the item but not enough for the removal
		playerItemDTO.setAmount(itemTooMany);
		Assert.assertEquals(500, playerService.removeItem(playerItemDTO).getCode());
		
		// happy path remove
		playerItemDTO.setAmount(itemStartWith);
		playerService.addItem(playerItemDTO);
		playerItemDTO.setAmount(itemAmount);
		Assert.assertEquals(200, playerService.removeItem(playerItemDTO).getCode());
		PlayerInventoryDO playerInventoryDO = playerInventoryMapper.selectOne(new QueryWrapper<>(playerInventorySearch));
		Assert.assertNotEquals(playerInventoryDO, null);
		Assert.assertTrue(playerInventoryDO.getAmount() == itemStartWith-itemAmount);
		
		// restore player inventory information
		playerInventoryMapper.deleteById(playerInventoryDO);
		if (oldPlayerInventory != null) {
			playerInventoryMapper.insert(oldPlayerInventory);
		}
	}
	
	@Test
	void testInventoryAdd() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		
		InventoryAddDTO addDTO = new InventoryAddDTO();
		addDTO.setToken(testToken);
		
		Assert.assertEquals(inventoryService.add(addDTO).getCode(), 200);
		
		String fakeToken = "fakeToken";
		addDTO = new InventoryAddDTO();
		addDTO.setToken(fakeToken);
		
		Assert.assertEquals(inventoryService.add(addDTO).getCode(), 500);
		
	}
	
	@Test
	void testInventoryInfo() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		String inventory = "INV20211205224845599";
		
		Assert.assertEquals(inventoryService.info(testToken, inventory).getCode(), 200);
		String fakeToken = "fakeToken";
		Assert.assertEquals(inventoryService.info(fakeToken, inventory).getCode(), 500);
		String fakeInventory = "fakeInventoryId";
		Assert.assertEquals(inventoryService.info(fakeToken, fakeInventory).getCode(), 500);
		
		
		
	}
	
	@Test
	void testInventoryClear() {
		String inventory = "INV202112081046200114";
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		
		InventoryClearDTO clearDTO = new InventoryClearDTO();
		clearDTO.setToken(testToken);
		clearDTO.setInventoryId(inventory);
		
		Assert.assertEquals(inventoryService.clear(clearDTO).getCode(), 200);
		String fakeToken = "fakeToken";
		clearDTO = new InventoryClearDTO();
		clearDTO.setToken(fakeToken);
		Assert.assertEquals(inventoryService.clear(clearDTO).getCode(), 500);
		clearDTO.setToken(testToken);
		String fakeInventory = "fakeInventoryId";
		clearDTO.setInventoryId(fakeInventory);
		
		Assert.assertEquals(inventoryService.clear(clearDTO).getCode(), 500);
		
	}
	
	@Test
	void testGameItemAdd() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		
		ItemAddDTO addDTO = new ItemAddDTO();
		addDTO.setToken(testToken);
		addDTO.setName("testItem");
		addDTO.setType(ItemTypeEnum.NOT_COM_AND_TRA.getType());
		addDTO.setPrice(1);
		addDTO.setInventoryId("INV202112081046200114");
		addDTO.setDescription("des");
		
		Assert.assertEquals(itemService.add(addDTO).getCode(), 200);
		addDTO.setToken("fakeToken");
		Assert.assertEquals(itemService.add(addDTO).getCode(), 500);
		addDTO.setToken(testToken);
		addDTO.setInventoryId("fakeInventory");
		Assert.assertEquals(itemService.add(addDTO).getCode(), 500);
	}
	
	@Test
	void testGameItemInfo() {
		GameManagerDO manager = new GameManagerDO();
		manager.setUsername(testUsername);
		String testToken = gameManagerMapper.selectOne(new QueryWrapper<>(manager)).getToken();
		
		String itemID = "ITEM2021120522492419219";
		
		Assert.assertEquals(itemService.info(testToken, itemID).getCode(), 200);
		Assert.assertEquals(itemService.info("fakeToken", itemID).getCode(), 500);
		Assert.assertEquals(itemService.info(testToken, "fakeId").getCode(), 500);
	}
	
}