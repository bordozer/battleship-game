package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.converter.CellConverter;
import com.bordozer.battleship.gameserver.converter.LogConverter;
import com.bordozer.battleship.gameserver.dto.GamePlayerDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.dto.battle.GameConfigDto;
import com.bordozer.battleship.gameserver.dto.battle.GameStep;
import com.bordozer.battleship.gameserver.dto.battle.GameplayDto;
import com.bordozer.battleship.gameserver.dto.battle.PlayerDto;
import com.bordozer.battleship.gameserver.dto.battle.PlayerType;
import com.bordozer.battleship.gameserver.exception.GameNotFoundException;
import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.GameState;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.service.BattleService;
import com.bordozer.battleship.gameserver.service.BattlefieldService;
import com.bordozer.battleship.gameserver.service.GameService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import com.bordozer.battleship.gameserver.utils.CellUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bordozer.battleship.gameserver.utils.ShipUtils.extractShips;

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
    public BattleDto getGameState(final String gameId, final String forPlayerId) {
        final var game = getGame(gameId);
        final var battle = game.getBattle();

        final var player = getPlayerDto(game);
        final var enemy = getOpponentDto(game);
        final var isForPlayer1 = isForPlayer1(forPlayerId, game);

        return BattleDto.builder()
                .player(isForPlayer1 ? player : enemy)
                .enemy(isForPlayer1 ? enemy : player)
                .config(getGameConfig())
                .gameplay(getGameplay(game, forPlayerId))
                .logs(LogConverter.getLogs(battle.getLogs()))
                .build();
    }

    @Override
    public void move(final String gameId, final String playerId, final PlayerMove move) {
        final var game = getGame(gameId);
        final var battlefield = getBattlefield(game, playerId);
        battlefieldService.move(game, battlefield, playerId, move);
    }

    private PlayerDto getPlayerDto(final Game game) {
        final var battle = game.getBattle();
        final var battlefield = battle.getBattlefield1();
        @CheckForNull final var lastShot = battlefield.getLastShot();

        final var player1 = playerService.getById(game.getPlayer1Id()).getName();
        final var cells = battlefield.getCells().stream()
                .map(CellConverter::convertCellsToDto)
                .collect(Collectors.toList());
        return PlayerDto.builder()
                .playerId(game.getPlayer1Id())
                .playerName(player1)
                .cells(cells)
                .ships(extractShips(game.getBattle().getBattlefield1().getCells()))
                .lastShot(convertLastShot(lastShot, cells))
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();
    }

    @CheckForNull
    private CellDto convertLastShot(@CheckForNull final PlayerMove lastShot, final List<List<CellDto>> cells) {
        return Optional.ofNullable(lastShot)
                .map(shot -> CellUtils.getCell(shot.getLine(), shot.getColumn(), cells))
                .orElse(null);
    }

    private PlayerDto getOpponentDto(final Game game) {
        final var battle = game.getBattle();
        final var battlefield = battle.getBattlefield2();
        final var lastShot = battlefield.getLastShot();

        final var player2 = getPlayer2(game);
        final var cells = battlefield.getCells().stream()
                .map(CellConverter::convertCellsToDto)
                .collect(Collectors.toList());
        return PlayerDto.builder()
                .playerId(player2.getId())
                .playerName(player2.getName())
                .cells(cells)
                .ships(extractShips(battlefield.getCells()))
                .lastShot(convertLastShot(lastShot, cells))
                .damagedShipCells(Collections.emptyList())
                .points(0)
                .build();
    }

    private GameConfigDto getGameConfig() {
        return GameConfigDto.builder()
                .difficulty(1)
                .showShotHints(false)
                .build();
    }

    private GameplayDto getGameplay(final Game game, final String forPlayerId) {
        final var gameId = game.getGameId();
        return GameplayDto.builder()
                .gameId(gameId)
                .step(convertGameStep(game.getState()))
                .currentMove(calculateCurrentMove(game, forPlayerId))
                .winner(calculateWinner(game, forPlayerId))
                .build();
    }

    @CheckForNull
    private PlayerType calculateCurrentMove(final Game game, final String forPlayerId) {
        final var player1Id = game.getPlayer1Id();
        @CheckForNull final var player2Id = game.getPlayer2Id();
        if (player2Id == null) {
            return null;
        }

        final var currentMove = game.getBattle().getCurrentMove();
        if (forPlayerId.equals(player1Id) && currentMove == PlayerType.PLAYER1) {
            return PlayerType.PLAYER1;
        }
        if (forPlayerId.equals(player1Id) && currentMove == PlayerType.PLAYER2) {
            return PlayerType.PLAYER2;
        }
        if (forPlayerId.equals(player2Id) && currentMove == PlayerType.PLAYER2) {
            return PlayerType.PLAYER1;
        }
        if (forPlayerId.equals(player2Id) && currentMove == PlayerType.PLAYER1) {
            return PlayerType.PLAYER2;
        }
        return null;
    }

    @CheckForNull
    private PlayerType calculateWinner(final Game game, final String forPlayerId) {
        final var winnerId = game.getWinnerId();
        if (StringUtils.isEmpty(winnerId)) {
            return null;
        }

        final var player1Id = game.getPlayer1Id();
        final var player2Id = game.getPlayer2Id();
        if (StringUtils.isEmpty(player2Id)) {
            return null;
        }

        if (forPlayerId.equals(player1Id) && winnerId.equals(player1Id)) {
            return PlayerType.PLAYER1;
        }
        if (forPlayerId.equals(player1Id) && winnerId.equals(player2Id)) {
            return PlayerType.PLAYER2;
        }
        if (forPlayerId.equals(player2Id) && winnerId.equals(player2Id)) {
            return PlayerType.PLAYER1;
        }
        if (forPlayerId.equals(player2Id) && winnerId.equals(player1Id)) {
            return PlayerType.PLAYER2;
        }
        return null;
    }

    private Battlefield getBattlefield(final Game game, final String playerId) {
        return game.getPlayer1Id().equals(playerId)
                ? game.getBattle().getBattlefield2()
                : game.getBattle().getBattlefield1();
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
                return GameStep.FINISHED;
            case CANCELLED:
                return GameStep.CANCELLED;
            default:
                throw new IllegalArgumentException(String.format("Unsupported game state: '%s'", state));
        }
    }

    private GamePlayerDto getPlayer2(final Game game) {
        return Optional.ofNullable(game.getPlayer2Id())
                .map(playerService::getById)
                .orElse(GamePlayerDto.builder()
                        .id(NO_OPPONENT_YET_ID)
                        .name(NO_OPPONENT_YET_NAME)
                        .build()
                );
    }

    private boolean isForPlayer1(final String forPlayerId, final Game game) {
        return forPlayerId.equals(game.getPlayer1Id());
    }
}
