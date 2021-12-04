package com.sangria.operation.dto;

import lombok.Data;

@Data
public class PlayerFreezeDTO {

    String token;
    String playerId;
    long balance;
    int status;

}
