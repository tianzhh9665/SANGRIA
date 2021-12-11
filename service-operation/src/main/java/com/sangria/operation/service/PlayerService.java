package com.sangria.operation.service;

import com.sangria.operation.dto.*;
import org.springframework.web.bind.annotation.RequestBody;

public interface PlayerService {

    ResponseDTO freeze(PlayerFreezeDTO dto);
    ResponseDTO unfreeze(PlayerUnfreezeDTO dto);
    ResponseDTO deletePlayer(PlayerDeleteDTO dto);
    
    ResponseDTO add(PlayerAddDTO dto);
    ResponseDTO info(String playerUuid, String token);
    ResponseDTO trade(PlayerTradeRequestDTO dto);

    ResponseDTO removeMoney(PlayerRemoveMoneyDTO dto);
    ResponseDTO buyItemSys(PlayerBuyItemSysDTO dto);
    ResponseDTO sellItemSys(PlayerSellItemSysDTO dto);
    
    ResponseDTO addMoney(PlayerAddMoneyDTO dto);
    ResponseDTO addItem(PlayerItemDTO dto);
    ResponseDTO removeItem(PlayerItemDTO dto);

}
