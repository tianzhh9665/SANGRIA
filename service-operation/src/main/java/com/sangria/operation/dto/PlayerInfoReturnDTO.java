package com.sangria.operation.dto;

import lombok.Data;

@Data
public class PlayerInfoReturnDTO {
	
	String uuid;
	String gameUuid;
	String gameInventoryUuid;
	Integer balance;
	String status;
	String createTime;
	String modifiedTime;

}
