package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.BattleDto;
import com.bordozer.battleship.gameserver.dto.ImmutableBattleDto;
import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BattleController {

    @MessageMapping("/player1/move")
    @SendTo("/player2/move")
    public BattleDto playerMove(final PlayerMoveDto move) {
        return ImmutableBattleDto.builder()
                .gameId(move.getGameId())
                .anotherPlayerMove(move)
                .build();
    }

    @MessageMapping("/player2/move")
    @SendTo("/player1/move")
    public BattleDto enemyMove(final PlayerMoveDto move) {
        return ImmutableBattleDto.builder()
                .gameId(move.getGameId())
                .anotherPlayerMove(move)
                .build();
    }
}
