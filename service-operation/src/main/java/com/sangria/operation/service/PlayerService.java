package com.sangria.operation.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.sangria.operation.dto.PlayerAddDTO;
import com.sangria.operation.dto.PlayerDeleteDTO;
import com.sangria.operation.dto.PlayerFreezeDTO;
import com.sangria.operation.dto.PlayerTradeRequestDTO;
import com.sangria.operation.dto.PlayerUnfreezeDTO;
import com.sangria.operation.dto.ResponseDTO;

public interface PlayerService {

    ResponseDTO freeze(PlayerFreezeDTO dto);
    ResponseDTO unfreeze(PlayerUnfreezeDTO dto);
    ResponseDTO deletePlayer(PlayerDeleteDTO dto);
    
    ResponseDTO add(PlayerAddDTO dto);
    ResponseDTO info(String playerUuid, String token);
    ResponseDTO trade(PlayerTradeRequestDTO dto);

}
