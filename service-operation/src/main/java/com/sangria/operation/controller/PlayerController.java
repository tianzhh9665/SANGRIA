package com.sangria.operation.controller;

import com.sangria.operation.dto.*;
import com.sangria.operation.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 *
 * @author Linyu Li
 *
 */
@RestController
@RequestMapping("/player")
public class PlayerController extends BaseController{

    @Autowired
    PlayerService playerService;

    /**
     * freeze a player
     * @param dto
     * @return
     */
    @PostMapping(value = "/freeze")
    public ResponseDTO freeze(@RequestBody PlayerFreezeDTO dto) {
        String token = dto.getToken();
        String playerId = dto.getPlayerId();
        if(StringUtils.isBlank(token)) {
            return renderFail("ERROR: authentication failed, token can not be empty");
        }
        if(StringUtils.isBlank(playerId)) {
            return renderFail("ERROR: playerId can not be empty");
        }

        return playerService.freeze(dto);
    }

    /**
     * unfreeze a player
     * @param dto
     * @return
     */
    @PostMapping(value = "/unfreeze")
    public ResponseDTO unfreeze(@RequestBody PlayerUnfreezeDTO dto) {
        String token = dto.getToken();
        String playerId = dto.getPlayerId();
        if(StringUtils.isBlank(token)) {
            return renderFail("ERROR: authentication failed, token can not be empty");
        }
        if(StringUtils.isBlank(playerId)) {
            return renderFail("ERROR: playerId can not be empty");
        }

        return playerService.unfreeze(dto);
    }
}
