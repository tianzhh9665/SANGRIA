package com.sangria.client.dto;

import lombok.Data;

@Data
public class PlayerAddMoneyDTO {

    String token;
    String playerUuid;
    Integer amount;

}
