package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.BattleDto;
import com.bordozer.battleship.gameserver.dto.ImmutableBattleDto;
import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BattleController {

    @MessageMapping("/inbound")
    @SendTo("/outbound")
    public BattleDto player1Move(final PlayerMoveDto move) {
        return constructResponse(move);
    }

    private BattleDto constructResponse(final PlayerMoveDto move) {
        return ImmutableBattleDto.builder()
                .gameId(move.getGameId())
                .playerMove(move)
                .build();
    }
}
