package com.sangria.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sangria.client.dto.ManagerRegDTO;
import com.sangria.client.utils.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sangria.client.dto.ManagerLoginDTO;
import com.sangria.client.dto.ManagerDeleteDTO;
import com.sangria.client.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 
 * @author Steven Huang
 *
 */
@Api(tags = "Auth Service Client API")
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController{
	
	@Value("${Service.ip}")
	private String IP;
	
	@Value("${Service.auth.port}")
	private String port;
	
	@Value("${Service.auth.context}")
	private String context;
	
	@Value("${Service.auth.apis.register}")
	private String registerAPI;
	
	@Value("${Service.auth.apis.login}")
	private String loginAPI;
	
	@Value("${Service.auth.apis.token}")
	private String tokenAPI;
	
	@Value("${Service.auth.apis.info}")
	private String infoAPI;
	
	@Value("${Service.auth.apis.delete}")
	private String deleteAPI;
	
	@Value("${Service.auth.apis.logout}")
	private String logoutAPI;

	@ApiOperation(value = "Register a new game Manager")
	@PostMapping(value = "/register")
	public JSONObject register(@RequestBody ManagerRegDTO dto) {
		String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
		String requestUrl = requestUrlHead + registerAPI;
		String paramJsonStr = JSON.toJSONString(dto);

		JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));
		
		return result;
	}
	
	@ApiOperation(value = "Game manager login")
	@PostMapping(value = "/login")
	public JSONObject login(@RequestBody ManagerLoginDTO dto) {
		String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
		String requestUrl = requestUrlHead + loginAPI;
		String paramJsonStr = JSON.toJSONString(dto);

		JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));
		
		return result;
	}
	
	@ApiOperation(value = "Query game manager and its managed game information")
	@GetMapping(value = "/info")
	public JSONObject info(String token) {
		String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
		String requestUrl = requestUrlHead + infoAPI + "?" + "token=" + token;
		
		JSONObject result = JSONObject.parseObject(HttpUtils.get(requestUrl));
		
		return result;
	}
	
	@ApiOperation(value = "Game manager logout")
	@PostMapping(value = "/logout")
	public JSONObject logout(@RequestBody ManagerDeleteDTO dto) {
		String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
		String requestUrl = requestUrlHead + logoutAPI;
		String paramJsonStr = JSON.toJSONString(dto);
		
		JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));
		
		return result;
	}
	
	@ApiOperation(value = "Game manager delete")
	@PostMapping(value = "/delete")
	public JSONObject delete(@RequestBody ManagerDeleteDTO dto) {
		String requestUrlHead = "http://" + IP + ":" + port + "/" + context + "/";
		String requestUrl = requestUrlHead + deleteAPI;
		String paramJsonStr = JSON.toJSONString(dto);
		
		JSONObject result = JSONObject.parseObject(HttpUtils.post(requestUrl, paramJsonStr));
		
		return result;
	}
}
