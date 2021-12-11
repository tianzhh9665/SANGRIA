package com.sangria.client.controller;

import com.sangria.client.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sangria.client.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sangria.client.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 *
 * @author Linyu Li
 *
 */
@Api(tags = "Operation Service Client API")
@RestController
@RequestMapping("/operation")
public class OperationController extends BaseController{

    @Value("${Service.ip}")
    private String IP;

    @Value("${Service.operation.port}")
    private String port;

    @Value("${Service.operation.context}")
    private String context;

    @Value("${Service.operation.apis.freeze}")
    private String freezeAPI;

    @Value("${Service.operation.apis.info}")
    private String infoAPI;

    @Value("${Service.operation.apis.removeMoney}")
    private String removeMoneyAPI;

    @Value("${Service.operation.apis.sellItemSys}")
    private String sellItemSysAPI;

    @Value("${Service.operation.apis.trade}")
    private String tradeAPI;

    @Value("${Service.operation.apis.unfreeze}")
    private String unfreezeAPI;

    @Value("${Service.operation.apis.buyItemSys}")
    private String buyItemSysAPI;

    @Value("${Service.operation.apis.deletePlayer}")
    private String deletePlayerAPI;

    @Value("${Service.operation.apis.inventoryAdd}")
    private String inventoryAddAPI;

    @Value("${Service.operation.apis.inventoryClear}")
    private String inventoryClearAPI;

    @Value("${Service.operation.apis.inventoryInfo}")
    private String inventoryInfoAPI;

    @Value("${Service.operation.apis.itemAdd}")
    private String itemAddAPI;

    @Value("${Service.operation.apis.itemInfo}")
    private String itemInfoAPI;

    @Value("${Service.operation.apis.itemMake}")
    private String itemMakeAPI;

    @ApiOperation(value = "Freeze a player, then the player can not do anything")
    @PostMapping(value = "/freeze")
    public JSONObject freeze(@RequestBody PlayerFreezeDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + freezeAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "query a player's info")
    @GetMapping(value = "/info")
    public JSONObject info(String playerUuid, String token) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + infoAPI + "?" + "playerUuid=" + playerUuid + "&token=" + token;

        JSONObject result = JSONObject.parseObject(HttpUtils.get(requestUrl));

        return result;
    }

    @ApiOperation(value = "reduce a player's money")
    @PostMapping(value = "/removeMoney")
    public JSONObject removeMoney(@RequestBody PlayerRemoveMoneyDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + removeMoneyAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "a player sells item and get money")
    @PostMapping(value = "/sellItemSys")
    public JSONObject sellItemSys(@RequestBody PlayerSellItemSysDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + sellItemSysAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "handle 4 types of trades between 2 players")
    @PostMapping(value = "/trade")
    public JSONObject trade(@RequestBody ManagerDeleteDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + tradeAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "Unfreeze a player, then the player's status becomes normal")
    @PostMapping(value = "/unfreeze")
    public JSONObject unfreeze(@RequestBody PlayerUnfreezeDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + unfreezeAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "a player buys item using money")
    @PostMapping(value = "/buyItemSys")
    public JSONObject buyItemSys(@RequestBody PlayerBuyItemSysDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + buyItemSysAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "Manager deletes a player")
    @PostMapping(value = "/deletePlayer")
    public JSONObject deletePlayer(@RequestBody PlayerDeleteDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + deletePlayerAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "add item to game inventory")
    @PostMapping(value = "/inventoryAdd")
    public JSONObject inventoryAdd(@RequestBody InventoryAddDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + inventoryAddAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "clear game inventory")
    @PostMapping(value = "/inventoryClear")
    public JSONObject inventoryClear(@RequestBody InventoryClearDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + inventoryClearAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "get game inventory info")
    @PostMapping(value = "/inventoryInfo")
    public JSONObject inventoryInfo(String token, String inventoryId) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + inventoryInfoAPI + "?" + "inventoryId=" + inventoryId + "&token=" + token;

        JSONObject result = JSONObject.parseObject(HttpUtils.get(requestUrl));

        return result;
    }

    @ApiOperation(value = "Add a new item to specified inventory")
    @PostMapping(value = "/itemAdd")
    public JSONObject itemAdd(@RequestBody ItemAddDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + itemAddAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

    @ApiOperation(value = "query an item's info")
    @PostMapping(value = "/itemInfo")
    public JSONObject itemInfo(String token, String itemId) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + itemInfoAPI + "?" + "itemId=" + itemId + "&token=" + token;

        JSONObject result = JSONObject.parseObject(HttpUtils.get(requestUrl));

        return result;
    }

    @ApiOperation(value = "make an item according to its ingredient list")
    @PostMapping(value = "/itemMake")
    public JSONObject itemMake(@RequestBody ItemMakeDTO dto) {
        String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
        String requestUrl = requestUrlHead + itemMakeAPI;
        String paramJsonStr = JSON.toJSONString(dto);

        JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));

        return result;
    }

}
