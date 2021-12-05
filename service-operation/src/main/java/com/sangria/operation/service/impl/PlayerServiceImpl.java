package com.sangria.operation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sangria.operation.dao.GameManagerMapper;
import com.sangria.operation.dao.PlayerMapper;
import com.sangria.operation.dto.PlayerDeleteDTO;
import com.sangria.operation.dto.PlayerFreezeDTO;
import com.sangria.operation.dto.PlayerUnfreezeDTO;
import com.sangria.operation.dto.ResponseDTO;
import com.sangria.operation.entity.GameManagerDO;
import com.sangria.operation.entity.PlayerDO;
import com.sangria.operation.service.PlayerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Resource
    private GameManagerMapper gameManagerMapper;

    @Resource
    private PlayerMapper playerMapper;

    @Override
    public ResponseDTO freeze(PlayerFreezeDTO dto) {
        String token = dto.getToken();
        GameManagerDO manager = new GameManagerDO();
        manager.setToken(token);

        List<GameManagerDO> managerList = gameManagerMapper.selectList(new QueryWrapper<>(manager));
        if(managerList == null || managerList.size() == 0) {
            return new ResponseDTO(500, "ERROR: token is not valid, please login first", null);
        }
        if(managerList != null && managerList.size() > 1) {
            return new ResponseDTO(500, "ERROR: more than one manager found, please try again later", null);
        }

        manager = managerList.get(0);
        String gameUuid = manager.getGameUuid();
        if(StringUtils.isBlank(gameUuid)) {
            return new ResponseDTO(500, "ERROR: this manager has not created a game yet", null);
        }

        PlayerDO playerSearch = new PlayerDO();
        playerSearch.setUuid(dto.getPlayerId());
        playerSearch.setGameUuid(gameUuid);
        playerSearch.setStatus("1");

        PlayerDO player = playerMapper.selectOne(new QueryWrapper<>(playerSearch));
        if (player == null) {
            return new ResponseDTO(500, "ERROR: the player has already been frozen or there is no qualified player", null);
        }

        player.setStatus("2");

        playerMapper.updateById(player);

        return new ResponseDTO(200, "player frozen successfully", null);
    }

    @Override
    public ResponseDTO unfreeze(PlayerUnfreezeDTO dto) {
        String token = dto.getToken();
        GameManagerDO manager = new GameManagerDO();
        manager.setToken(token);

        List<GameManagerDO> managerList = gameManagerMapper.selectList(new QueryWrapper<>(manager));
        if(managerList == null || managerList.size() == 0) {
            return new ResponseDTO(500, "ERROR: token is not valid, please login first", null);
        }
        if(managerList != null && managerList.size() > 1) {
            return new ResponseDTO(500, "ERROR: more than one manager found, please try again later", null);
        }

        manager = managerList.get(0);
        String gameUuid = manager.getGameUuid();
        if(StringUtils.isBlank(gameUuid)) {
            return new ResponseDTO(500, "ERROR: this manager has not created a game yet", null);
        }

        PlayerDO playerSearch = new PlayerDO();
        playerSearch.setUuid(dto.getPlayerId());
        playerSearch.setGameUuid(gameUuid);
        playerSearch.setStatus("2");

        PlayerDO player = playerMapper.selectOne(new QueryWrapper<>(playerSearch));
        if (player == null) {
            return new ResponseDTO(500, "ERROR: the player has already been unfrozen or there is no qualified player", null);
        }

        player.setStatus("1");

        playerMapper.updateById(player);

        return new ResponseDTO(200, "player unfrozen successfully", null);
    }

    @Override
    public ResponseDTO deletePlayer(PlayerDeleteDTO dto){
        String token = dto.getToken();
        GameManagerDO manager = new GameManagerDO();
        manager.setToken(token);

        List<GameManagerDO> managerList = gameManagerMapper.selectList(new QueryWrapper<>(manager));
        if(managerList == null || managerList.size() == 0) {
            return new ResponseDTO(500, "ERROR: token is not valid, please login first", null);
        }
        if(managerList != null && managerList.size() > 1) {
            return new ResponseDTO(500, "ERROR: more than one manager found, please try again later", null);
        }

        manager = managerList.get(0);
        String gameUuid = manager.getGameUuid();
        if(StringUtils.isBlank(gameUuid)) {
            return new ResponseDTO(500, "ERROR: this manager has not created a game yet", null);
        }

        PlayerDO playerDeleted = new PlayerDO();
        playerDeleted.setUuid(dto.getPlayerId());
        playerDeleted.setGameUuid(gameUuid);

        List<PlayerDO> playerList = playerMapper.selectList(new QueryWrapper<>(playerDeleted));
        if(playerList == null || playerList.size() == 0) {
            return new ResponseDTO(500, "ERROR: there is no qualified player, try again", null);
        }
        if(playerList != null && playerList.size() > 1) {
            return new ResponseDTO(500, "ERROR: more than one player found, please try again later", null);
        }

        playerDeleted = playerList.get(0);

        if(playerMapper.delete(new QueryWrapper<>(playerDeleted)) <= 0) {
            return new ResponseDTO(500, "ERROR: no player has been deleted", null);
        }

        return new ResponseDTO(200, "player has been deleted successfully", null);
    }

}
