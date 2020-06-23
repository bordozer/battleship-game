package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.dto.battle.GameConfigDto;
import com.bordozer.battleship.gameserver.dto.battle.GameStep;
import com.bordozer.battleship.gameserver.dto.battle.GameplayDto;
import com.bordozer.battleship.gameserver.dto.battle.PlayerDto;
import com.bordozer.battleship.gameserver.service.BattleService;
import com.bordozer.battleship.gameserver.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

import static com.bordozer.battleship.gameserver.dto.battle.CurrentMove.PLAYER;

@Service
@RequiredArgsConstructor
public class BattleServiceImpl implements BattleService {

    private final GameService gameService;

    @Override
    public BattleDto getBattle(final String gameId) {
        final var game = gameService.getGame(gameId);

        final var player = PlayerDto.builder()
                .playerId(game.getPlayer1Id())
                .name("player")
                .cells(Collections.emptyList())
                .ships(Collections.emptyList())
                .lastShot(null)
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();

        final var player2Id = game.getPlayer2Id();
        Objects.requireNonNull(player2Id, "Player1 cannot be null at this step");
        final var enemy = PlayerDto.builder()
                .playerId(player2Id)
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
                .gameId(gameId)
                .player(player)
                .enemy(enemy)
                .config(gameConfig)
                .gameplay(gameplay)
                .logs(Collections.emptyList())
                .build();
    }
}
