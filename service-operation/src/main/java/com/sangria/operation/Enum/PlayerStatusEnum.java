package com.sangria.operation.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerStatusEnum {
	
	NORMAL("1","normal"),
	FROZEN("2","frozen");

	String status;
	String message;
}
