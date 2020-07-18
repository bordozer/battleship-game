package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.dto.GameDto;
import com.bordozer.battleship.multiplayer.dto.battle.CellDto;
import com.bordozer.battleship.multiplayer.exception.IncopatibleGameStatusException;
import com.bordozer.battleship.multiplayer.exception.GameNotFoundException;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.GamePlayers;
import com.bordozer.battleship.multiplayer.model.GameState;
import com.bordozer.battleship.multiplayer.model.LogItem;
import com.bordozer.battleship.multiplayer.service.GameService;
import com.bordozer.battleship.multiplayer.service.IdentityService;
import com.bordozer.battleship.multiplayer.service.PlayerService;
import com.bordozer.battleship.multiplayer.utils.BattleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.bordozer.battleship.multiplayer.converter.CellConverter.convertCells;
import static com.bordozer.battleship.multiplayer.converter.GameConverter.toDto;
import static com.bordozer.battleship.multiplayer.dto.battle.PlayerType.PLAYER1;
import static com.bordozer.battleship.multiplayer.model.GameState.CANCELLED;
import static com.bordozer.battleship.multiplayer.model.GameState.FINISHED;
import static com.bordozer.battleship.multiplayer.model.GameState.OPEN;
import static com.bordozer.battleship.multiplayer.utils.RandomUtils.randomizeFirstMove;
import static com.bordozer.battleship.multiplayer.utils.SecurityUtils.assertAccess;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final static Map<String, Game> GAME_MAP = new ConcurrentHashMap<>();

    private final PlayerService playerService;
    private final IdentityService identityService;

    @Override
    public List<GameDto> getGames(final String playerId) {
        return GAME_MAP.keySet().stream()
                .filter(gameId -> {
                    final var game = GAME_MAP.get(gameId);
                    final var state = game.getState();
                    if (state == FINISHED) {
                        return false;
                    }
                    if (state == CANCELLED) {
                        return false;
                    }
                    if (state == OPEN) {
                        return true;
                    }
                    // state is BATTLE
                    return game.getPlayer1Id().equals(playerId) || (game.getPlayer2Id() != null && game.getPlayer2Id().equals(playerId));
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDto> getPlayerGames(final String playerId) {
        return GAME_MAP.keySet().stream()
                .filter(gameId -> {
                    final var game = GAME_MAP.get(gameId);
                    final var state = game.getState();
                    if (state == FINISHED) {
                        return false;
                    }
                    if (state == CANCELLED) {
                        return false;
                    }
                    return game.getPlayer1Id().equals(playerId) || (game.getPlayer2Id() != null && game.getPlayer2Id().equals(playerId));
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GameDto> getOpenGames(final String playerId) {
        return GAME_MAP.keySet().stream()
                .filter(gameId -> {
                    final var game = GAME_MAP.get(gameId);
                    final var state = game.getState();
                    if (state == FINISHED) {
                        return false;
                    }
                    if (state == CANCELLED) {
                        return false;
                    }
                    if (game.getPlayer1Id().equals(playerId)) {
                        return false;
                    }
                    if (game.getPlayer2Id() != null && game.getPlayer2Id().equals(playerId)) {
                        return false;
                    }
                    return state == OPEN;
                })
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GameDto createGame(final String playerId, final ArrayList<ArrayList<CellDto>> cells) {
        final var gameId = identityService.generateForGame();
        final var game = Game.builder()
                .gameId(gameId)
                .created(LocalDateTime.now())
                .player1Id(playerId)
                .state(OPEN)
                .battle(BattleUtils.initBattle(cells))
                .build();

        GAME_MAP.put(gameId, game);

        final var player1 = playerService.getById(playerId);
        return toDto(game, player1, null);
    }

    @Override
    public void joinGame(final String gameId, final String playerId, final ArrayList<ArrayList<CellDto>> cells) {
        if (!isOpenGame(gameId)) {
            throw new IncopatibleGameStatusException(gameId);
        }
        synchronized (GAME_MAP.get(gameId)) {
            final var game = GAME_MAP.get(gameId);
            if (!isOpenGame(gameId)) {
                throw new IncopatibleGameStatusException(gameId);
            }

            game.setPlayer2Id(playerId);
            game.setState(GameState.BATTLE);

            final var battle = game.getBattle();
            battle.getBattlefield2().setCells(convertCells(cells));

            final var firstMove = randomizeFirstMove();
            battle.setCurrentMove(firstMove);
            battle
                    .addLog(LogItem.builder().text(String.format("Player %s joined the game", getPlayerName(playerId))).build())
                    .addLog(LogItem.builder().text(String.format("First move: %s", firstMove == PLAYER1 ? "Player 1" : "Player 2")).build());
        }
    }

    private String getPlayerName(final String playerId) {
        return playerService.getById(playerId).getName();
    }

    @Override
    public Game getGame(final String gameId) {
        if (!GAME_MAP.containsKey(gameId)) {
            throw new GameNotFoundException(gameId);
        }
        return GAME_MAP.get(gameId);
    }

    @Override
    public GamePlayers getGamePlayers(final String gameId) {
        final var game = getGame(gameId);
        return GamePlayers.of(game.getPlayer1Id(), game.getPlayer2Id());
    }

    @Override
    public void cancelGame(final String gameId, final String playerId) {
        final var game = getGame(gameId);
        assertAccess(game, playerId);
        game.setState(CANCELLED);
    }

    private boolean isOpenGame(final String gameId) {
        final var aGame = GAME_MAP.get(gameId);
        return aGame.getState() == OPEN;
    }

    private GameDto convertToDto(final String gameId) {
        final var game = GAME_MAP.get(gameId);
        final var player1 = playerService.getById(game.getPlayer1Id());
        final var player2 = game.getPlayer2Id() != null ? playerService.getById(game.getPlayer2Id()) : null;
        return toDto(game, player1, player2);
    }
}
