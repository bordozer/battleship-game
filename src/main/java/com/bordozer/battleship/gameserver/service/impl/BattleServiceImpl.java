package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.dto.battle.GameConfigDto;
import com.bordozer.battleship.gameserver.dto.battle.GameStep;
import com.bordozer.battleship.gameserver.dto.battle.GameplayDto;
import com.bordozer.battleship.gameserver.dto.battle.LogDto;
import com.bordozer.battleship.gameserver.dto.battle.PlayerDto;
import com.bordozer.battleship.gameserver.exception.GameNotFoundException;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.LogItem;
import com.bordozer.battleship.gameserver.service.BattleService;
import com.bordozer.battleship.gameserver.service.GameService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bordozer.battleship.gameserver.dto.battle.CurrentMove.PLAYER1;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleServiceImpl implements BattleService {

    private final GameService gameService;
    private final PlayerService playerService;

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

        final var player1 = PlayerDto.builder()
                .playerId(game.getPlayer1Id())
                .name(playerService.getById(game.getPlayer1Id()).getName())
                .cells(player1Cells)
                .ships(Collections.emptyList())
                .lastShot(null)
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();

        final var player2 = game.getPlayer2Id();
        Objects.requireNonNull(player2, "Player2 cannot be null at this step");
        final var enemy = PlayerDto.builder()
                .playerId(player2)
                .name(playerService.getById(game.getPlayer2Id()).getName())
                .cells(player2Cells)
                .ships(Collections.emptyList())
                .lastShot(null)
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();

        final var gameConfig = GameConfigDto.builder()
                .difficulty(1)
                .showShotHints(false)
                .build();
        final var gameplay = GameplayDto.builder()
                .gameId(gameId)
                .step(GameStep.GAME_INIT)
                .currentMove(battle.getCurrentMove())
                .build();

        return BattleDto.builder()
                .player(player1)
                .enemy(enemy)
                .config(gameConfig)
                .gameplay(gameplay)
                .logs(convertLogs(battle.getLogs()))
                .build();
    }

    private List<LogDto> convertLogs(final List<LogItem> logs) {
        return logs.stream()
                .map(log -> LogDto.builder().time(log.getTime()).text(log.getText()).build())
                .collect(Collectors.toList());
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
