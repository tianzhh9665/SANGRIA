package com.sangria.client.dto;

import lombok.Data;

@Data
public class PlayerRemoveMoneyDTO {

    String token;
    String playerUuid;
    Integer amount;

}
