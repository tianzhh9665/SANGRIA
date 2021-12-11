package com.sangria.client.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemTypeEnum {
	
	COM_AND_TRA(1, "compostable & tradable"),
	
	COM_AND_NOT_TRA(2, "compostable & NOT tradable"),
	
	NOT_COM_AND_TRA(3, " NOT compostable & tradable"),
	
	NOT_COM_AND_NOT_TRA(4, " NOT compostable & NOT tradable");
	
	int type;

    String message;

}
