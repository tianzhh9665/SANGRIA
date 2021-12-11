package com.sangria.client.dto;

import lombok.Data;

@Data
public class PlayerSellItemSysDTO {

    String token;
    String playerUuid;
    String itemUuid;
    Integer amount;

}
