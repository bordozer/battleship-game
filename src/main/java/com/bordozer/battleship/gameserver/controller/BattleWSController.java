package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.dto.battle.GameConfigDto;
import com.bordozer.battleship.gameserver.dto.battle.GameStep;
import com.bordozer.battleship.gameserver.dto.battle.GameplayDto;
import com.bordozer.battleship.gameserver.dto.battle.PlayerDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Collections;

import static com.bordozer.battleship.gameserver.dto.battle.CurrentMove.PLAYER;

@Controller
public class BattleWSController {

    @MessageMapping("/inbound")
    @SendTo("/outbound")
    public BattleDto player1Move(final PlayerMoveDto move) {
        return constructResponse(move);
    }

    private BattleDto constructResponse(final PlayerMoveDto move) {
        final var player = PlayerDto.builder()
                .playerId(move.getPlayerId())
                .name("player")
                .cells(Collections.emptyList())
                .ships(Collections.emptyList())
                .lastShot(null)
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();
        final var enemy = PlayerDto.builder()
                .playerId(move.getPlayerId())
                .name("enemy")
                .cells(Collections.emptyList())
                .ships(Collections.emptyList())
                .lastShot(null)
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();
        final var gameConfig = GameConfigDto.builder()
                .difficulty(0)
                .showShotHints(false)
                .build();
        final var gameplay = GameplayDto.builder()
                .step(GameStep.STEP_READY_TO_START)
                .currentMove(PLAYER)
                .build();

        return BattleDto.builder()
                .gameId(move.getGameId())
                .player(player)
                .enemy(enemy)
                .config(gameConfig)
                .gameplay(gameplay)
                .logs(Collections.emptyList())
                .build();
    }
}
