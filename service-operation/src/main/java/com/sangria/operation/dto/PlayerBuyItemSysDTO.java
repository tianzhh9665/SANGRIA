package com.sangria.operation.dto;

import lombok.Data;

@Data
public class PlayerBuyItemSysDTO {

    String token;
    String playerUuid;
    String itemUuid;
    Integer amount;

}
