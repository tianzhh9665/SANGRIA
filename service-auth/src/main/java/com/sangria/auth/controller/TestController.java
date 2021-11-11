package com.sangria.auth.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sangria.auth.dto.ResponseDTO;

@RestController
@RequestMapping("/test")
public class TestController {
	
	@GetMapping(value = "/index")
	public ResponseDTO index() {
		return new ResponseDTO(200,"success",null);
	}
}
