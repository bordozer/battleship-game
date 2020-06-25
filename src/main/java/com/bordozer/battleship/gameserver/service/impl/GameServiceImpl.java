package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.exception.GameNotFoundException;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.GamePlayers;
import com.bordozer.battleship.gameserver.model.GameState;
import com.bordozer.battleship.gameserver.model.LogItem;
import com.bordozer.battleship.gameserver.service.GameService;
import com.bordozer.battleship.gameserver.service.IdentityService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import com.bordozer.battleship.gameserver.utils.BattleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.bordozer.battleship.gameserver.converter.CellConverter.convertCells;
import static com.bordozer.battleship.gameserver.converter.GameConverter.toDto;
import static com.bordozer.battleship.gameserver.dto.battle.PlayerType.PLAYER1;
import static com.bordozer.battleship.gameserver.model.GameState.BATTLE;
import static com.bordozer.battleship.gameserver.model.GameState.OPEN;
import static com.bordozer.battleship.gameserver.utils.RandomUtils.randomizeFirstMove;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final static Map<String, Game> GAME_MAP = new ConcurrentHashMap<>();

    private final PlayerService playerService;
    private final IdentityService identityService;

    @Override
    public List<GameDto> getOpenGames() {
        return GAME_MAP.keySet().stream()
                .filter(gameId -> GAME_MAP.get(gameId).getState() == OPEN)
                .map(gameId -> {
                    synchronized (GAME_MAP.get(gameId)) {
                        return convertToDto(gameId);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public GameDto create(final String playerId, final ArrayList<ArrayList<CellDto>> cells) {
        final var gameId = identityService.generateForGame();
        final var game = Game.builder()
                .gameId(gameId)
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
        if (!canJoin(gameId, playerId)) {
            throw new IllegalStateException("Wrong game state");
        }
        synchronized (GAME_MAP.get(gameId)) {
            final var game = GAME_MAP.get(gameId);
            if (!canJoin(gameId, playerId)) {
                throw new IllegalStateException("Wrong game state");
            }
            if (!isRejoinPlayer2(playerId, game)) {
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
    }

    private String getPlayerName(final String playerId) {
        return playerService.getById(playerId).getName();
    }

    private boolean canJoin(final String gameId, final String playerId) {
        final var aGame = GAME_MAP.get(gameId);
        return isJoinNewOpenGame(aGame) || isRejoinPlayer2(playerId, aGame);
    }

    private boolean isJoinNewOpenGame(final Game aGame) {
        return aGame.getState() == OPEN && aGame.getPlayer2Id() == null;
    }

    private boolean isRejoinPlayer2(final String playerId, final Game aGame) {
        return aGame.getState() == BATTLE && playerId.equals(aGame.getPlayer2Id());
    }

    @Override
    @CheckForNull
    public Game getGame(final String gameId) {
        return GAME_MAP.get(gameId);
    }

    @Override
    public GamePlayers getGamePlayers(final String gameId) {
        final var game = getGame(gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        return GamePlayers.of(game.getPlayer1Id(), game.getPlayer2Id());
    }

    private GameDto convertToDto(final String gameId) {
        final var game = GAME_MAP.get(gameId);
        final var player1 = playerService.getById(game.getPlayer1Id());
        final var player2 = game.getPlayer2Id() != null ? playerService.getById(game.getPlayer2Id()) : null;
        return toDto(game, player1, player2);
    }
}
