package com.sangria.operation.dto;

import lombok.Data;

@Data
public class ItemAddDTO {
	
	String token;
	String inventoryId;
	String name;
	String type;
	String attributes;

}
