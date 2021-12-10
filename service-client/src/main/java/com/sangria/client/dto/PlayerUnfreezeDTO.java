package com.sangria.client.dto;

import lombok.Data;

@Data
public class PlayerUnfreezeDTO {

    String token;
    String playerId;
    int status;

}
