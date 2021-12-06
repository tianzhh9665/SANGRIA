package com.sangria.operation.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerTradeWayEnum {
	
	INCREASE(1, "get money"),
	DECREASE(2, "spend money");

	int tradeWay;
	String message;
}
