package com.sangria.client.dto;

import lombok.Data;

@Data
public class PlayerFreezeDTO {

    String token;
    String playerId;
    int status;

}
