package com.sangria.operation.service;

import com.sangria.operation.dto.ResponseDTO;

public interface PlayerService {

    ResponseDTO freeze();
    ResponseDTO unfreeze();
    ResponseDTO deletePlayer();

}
