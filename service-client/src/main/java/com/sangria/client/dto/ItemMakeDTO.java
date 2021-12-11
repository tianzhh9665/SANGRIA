package com.sangria.client.dto;

import lombok.Data;

@Data
public class ItemMakeDTO {
	
	String playerUuid;
	String itemUuid;
	Integer amount;
	String token;

}
