package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.BattleDto;
import com.bordozer.battleship.gameserver.dto.ImmutableBattleDto;
import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BattleController {

    @MessageMapping("/move/player1")
    @SendTo("/player2/player1-move")
    public BattleDto player1Move(final PlayerMoveDto move) {
        return constructResponse(move);
    }

    @MessageMapping("/move/player2")
    @SendTo("/player1/player2-move")
    public BattleDto player2Move(final PlayerMoveDto move) {
        return constructResponse(move);
    }

    private ImmutableBattleDto constructResponse(final PlayerMoveDto move) {
        return ImmutableBattleDto.builder()
                .gameId(move.getGameId())
                .anotherPlayerMove(move)
                .build();
    }
}
