package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GamePlayerDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.dto.battle.GameConfigDto;
import com.bordozer.battleship.gameserver.dto.battle.GameStep;
import com.bordozer.battleship.gameserver.dto.battle.GameplayDto;
import com.bordozer.battleship.gameserver.dto.battle.ImmutableShipDto;
import com.bordozer.battleship.gameserver.dto.battle.LogDto;
import com.bordozer.battleship.gameserver.dto.battle.PlayerDto;
import com.bordozer.battleship.gameserver.dto.battle.ShipDto;
import com.bordozer.battleship.gameserver.exception.GameNotFoundException;
import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.GameState;
import com.bordozer.battleship.gameserver.model.LogItem;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.model.Ship;
import com.bordozer.battleship.gameserver.service.BattleService;
import com.bordozer.battleship.gameserver.service.BattlefieldService;
import com.bordozer.battleship.gameserver.service.GameService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattleServiceImpl implements BattleService {

    private static final String NO_OPPONENT_YET_ID = "no-opponent-yet-id";
    private static final String NO_OPPONENT_YET_NAME = "unknown yet";

    private final GameService gameService;
    private final PlayerService playerService;
    private final BattlefieldService battlefieldService;

    @Override
    public BattleDto getBattle(final String gameId) {
        final var game = getGame(gameId);

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
                .ships(convertShips(game.getBattle().getBattlefield1().getCells()))
                .lastShot(null)
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();

        final var player2 = getOpponent(game);
        final var enemy = PlayerDto.builder()
                .playerId(player2.getId())
                .name(player2.getName())
                .cells(player2Cells)
                .ships(convertShips(game.getBattle().getBattlefield2().getCells()))
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
                .step(convertGameStep(game.getState()))
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

    @Override
    public void move(final String gameId, final String playerId, final PlayerMove move) {
        final var game = getGame(gameId);
        final var battle = game.getBattle();

        final var battlefield = getBattlefield(game, playerId);
        battlefieldService.move(battlefield, move);
    }

    private Battlefield getBattlefield(final Game game, final String playerId) {
        return game.getPlayer1Id().equals(playerId) ? game.getBattle().getBattlefield2() : game.getBattle().getBattlefield1();
    }

    private Game getGame(final String gameId) {
        @CheckForNull final var game = gameService.getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        return game;
    }

    private GameStep convertGameStep(final GameState state) {
        switch (state) {
            case OPEN:
                return GameStep.WAITING_FOR_OPPONENT;
            case BATTLE:
                return GameStep.BATTLE;
            case FINISHED:
                return GameStep.FINAL;
            default:
                throw new IllegalArgumentException(String.format("Unsupported game state: '%s'", state));
        }
    }

    private GamePlayerDto getOpponent(final Game game) {
        return Optional.ofNullable(game.getPlayer2Id())
                .map(playerService::getById)
                .orElse(GamePlayerDto.builder()
                        .id(NO_OPPONENT_YET_ID)
                        .name(NO_OPPONENT_YET_NAME)
                        .build()
                );
    }

    /* TODO: move to converter */
    private static List<? extends ShipDto> convertShips(final List<List<BattlefieldCell>> cells) {
        return cells.stream()
                .flatMap(Collection::stream)
                .map(BattlefieldCell::getShip)
                .filter(Objects::nonNull)
                .distinct()
                .map(BattleServiceImpl::convertToShipDto)
                .collect(Collectors.toList());
    }

    /* TODO: move to converter */
    private static ImmutableShipDto convertToShipDto(final Ship ship) {
        return ShipDto.builder()
                .id(ship.getShipId())
                .name(ship.getName())
                .size(ship.getSize())
                .damage(ship.getDamage())
                .cells(Collections.emptyList())
                .build();
    }

    /* TODO: move to converter */
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
                .ship(convertShip(cell.getShip()))
                .build();
    }

    /* TODO: move to converter */
    @CheckForNull
    private ShipDto convertShip(@CheckForNull final Ship ship) {
        return Optional.ofNullable(ship)
                .map(s -> ShipDto.builder()
                        .id(s.getShipId())
                        .name(s.getName())
                        .size(s.getSize())
                        .damage(s.getDamage())
                        .build())
                .orElse(null);
    }
}
