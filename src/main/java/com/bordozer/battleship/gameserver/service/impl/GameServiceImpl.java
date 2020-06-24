package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.GamePlayerDto;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.GameState;
import com.bordozer.battleship.gameserver.service.GameService;
import com.bordozer.battleship.gameserver.service.IdentityService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import com.bordozer.battleship.gameserver.utils.BattleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.bordozer.battleship.gameserver.converter.GameConverter.toDto;
import static com.bordozer.battleship.gameserver.model.GameState.OPEN;

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
    public GameDto create(final String playerId) {
        final var gameId = identityService.generateForGame();
        final var game = Game.builder()
                .gameId(gameId)
                .player1Id(playerId)
                .state(OPEN)
                .battle(BattleUtils.initBattle())
                .build();

        GAME_MAP.put(gameId, game);

        final var player1 = playerService.getById(playerId);
        return toDto(game, player1, null);
    }

    @Override
    public void joinGame(final String gameId, final String playerId) {
        if (GAME_MAP.get(gameId).getState() != OPEN) {
            throw new IllegalStateException("Game is busy");
        }
        synchronized (GAME_MAP.get(gameId)) {
            final var game = GAME_MAP.get(gameId);
            if (game.getState() != OPEN) {
                throw new IllegalStateException("Game is busy");
            }
            game.setPlayer2Id(playerId);
            game.setState(GameState.BATTLE);
        }
    }

    @Override
    @CheckForNull
    public Game getGame(final String gameId) {
        return GAME_MAP.get(gameId);
    }

    /* TODO: move to converter */
    @CheckForNull
    private GamePlayerDto convertPlayer(@CheckForNull final GamePlayerDto player) {
        if (player == null) {
            return null;
        }
        return GamePlayerDto.builder()
                .id(player.getId())
                .name(player.getName())
                .build();
    }

    private GameDto convertToDto(final String gameId) {
        final var game = GAME_MAP.get(gameId);
        final var player1 = playerService.getById(game.getPlayer1Id());
        final var player2 = game.getPlayer2Id() != null ? playerService.getById(game.getPlayer2Id()) : null;
        return toDto(game, player1, player2);
    }
}
