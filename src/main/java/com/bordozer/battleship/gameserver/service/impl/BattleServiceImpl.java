package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.dto.battle.GameConfigDto;
import com.bordozer.battleship.gameserver.dto.battle.GameStep;
import com.bordozer.battleship.gameserver.dto.battle.GameplayDto;
import com.bordozer.battleship.gameserver.dto.battle.PlayerDto;
import com.bordozer.battleship.gameserver.exception.GameNotFoundException;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.service.BattleService;
import com.bordozer.battleship.gameserver.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bordozer.battleship.gameserver.dto.battle.CurrentMove.PLAYER;

@Service
@RequiredArgsConstructor
public class BattleServiceImpl implements BattleService {

    private final GameService gameService;

    @Override
    public BattleDto getBattle(final String gameId) {
        @CheckForNull final var game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }

        final var battle = game.getBattle();
        final var player1Cells = battle.getBattlefield1().getCells().stream()
                .map(this::convertCellsToDto)
                .collect(Collectors.toList());
        final var player2Cells = battle.getBattlefield2().getCells().stream()
                .map(this::convertCellsToDto)
                .collect(Collectors.toList());

        final var player = PlayerDto.builder()
                .playerId(game.getPlayer1Id())
                .name("player")
                .cells(player1Cells)
                .ships(Collections.emptyList())
                .lastShot(null)
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();

        final var player2Id = game.getPlayer2Id();
        Objects.requireNonNull(player2Id, "Player2 cannot be null at this step");
        final var enemy = PlayerDto.builder()
                .playerId(player2Id)
                .name("enemy")
                .cells(player2Cells)
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

    /* TODO: move to converter */
    private List<CellDto> convertCellsToDto(final List<BattlefieldCell> columns) {
        return columns.stream()
                .map(this::convertCellToDto)
                .collect(Collectors.toList());
    }

    /* TODO: move to converter */
    private CellDto convertCellToDto(final BattlefieldCell cell) {
        return CellDto.builder()
                .x(cell.getColumn())
                .y(cell.getLine())
                .xLabel(cell.getXLabel())
                .yLabel(cell.getYLabel())
                .isHit(Boolean.TRUE.equals(cell.isHit()))
                .isShipNeighbor(Boolean.TRUE.equals(cell.isShipNeighbor()))
                .isKilledShipNeighborCell(Boolean.TRUE.equals(cell.isKilledShipNeighbor()))
                .build();
    }
}
