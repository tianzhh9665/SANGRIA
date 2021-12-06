package com.sangria.operation.dto;

import lombok.Data;

@Data
public class PlayerRemoveMoneyDTO {

    String token;
    String playerUuid;
    Integer amount;

}
