package com.sangria.client.dto;

import java.util.List;

import lombok.Data;

@Data
public class ItemAddDTO {
	
	String token;
	String inventoryId;
	String name;
	Integer type;
	Integer price;
	List<List> ingredients;
	String description;

}
