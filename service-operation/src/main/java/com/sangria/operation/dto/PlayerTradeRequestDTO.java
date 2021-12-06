package com.sangria.operation.dto;

import java.util.List;

import lombok.Data;

@Data
public class PlayerTradeRequestDTO {

	Integer type;
	String player1Uuid;
	Integer player1Amount;
	String player2Uuid;
	Integer player2Amount;
	List<List> player1ItemList;
	List<List> player2ItemList;
	String token;
	
}
