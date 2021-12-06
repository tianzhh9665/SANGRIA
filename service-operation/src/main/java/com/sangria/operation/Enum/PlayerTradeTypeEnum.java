package com.sangria.operation.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerTradeTypeEnum {

	MONEY_TO_MONEY(1, "player 1 money to player 2 money"),
	MONEY_TO_ITEM(2, "player 1 money to player 2 item"),
	ITEM_TO_MONEY(3, "player 1 item to player 2 money"),
	ITEM_TO_ITEM(4, "player 1 item to player 2 item");
	
	int type;
	String message;
}
