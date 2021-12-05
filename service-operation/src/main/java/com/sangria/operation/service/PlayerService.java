package com.sangria.operation.service;

import com.sangria.operation.dto.PlayerDeleteDTO;
import com.sangria.operation.dto.PlayerFreezeDTO;
import com.sangria.operation.dto.PlayerUnfreezeDTO;
import com.sangria.operation.dto.ResponseDTO;

public interface PlayerService {

    ResponseDTO freeze(PlayerFreezeDTO dto);
    ResponseDTO unfreeze(PlayerUnfreezeDTO dto);
    ResponseDTO deletePlayer(PlayerDeleteDTO dto);

}
