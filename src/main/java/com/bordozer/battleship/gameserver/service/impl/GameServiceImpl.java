package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.GamePlayerDto;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.GameState;
import com.bordozer.battleship.gameserver.service.GameService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import com.bordozer.battleship.gameserver.utils.BattleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.bordozer.battleship.gameserver.converter.GameConverter.toDto;
import static com.bordozer.battleship.gameserver.model.GameState.OPEN;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final Map<String, Game> gamesMap = new ConcurrentHashMap<>();

    private final PlayerService playerService;

    @Override
    public List<GameDto> getOpenGames() {
        return gamesMap.keySet().stream()
                .filter(gameId -> gamesMap.get(gameId).getState() == OPEN)
                .map(gameId -> {
                    synchronized (gamesMap.get(gameId)) {
                        return convertToDto(gameId);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public GameDto create(final String playerId) {
        final var gameId = UUID.randomUUID().toString();
        final var game = Game.builder()
                .gameId(gameId)
                .player1Id(playerId)
                .state(OPEN)
                .battle(BattleUtils.initBattle())
                .build();

        gamesMap.put(gameId, game);

        final var player1 = playerService.getById(playerId);
        return toDto(game, player1, null);
    }

    @Override
    public GameDto joinGame(final String gameId, final String playerId) {
        if (gamesMap.get(gameId).getState() != OPEN) {
            throw new IllegalStateException("Game is busy");
        }
        synchronized (gamesMap.get(gameId)) {
            final var game = gamesMap.get(gameId);
            if (game.getState() != OPEN) {
                throw new IllegalStateException("Game is busy");
            }
            game.setPlayer2Id(playerId);
            game.setState(GameState.BATTLE);
        }

        return convertToDto(gameId);
    }

    @Override
    public Game getGame(final String gameId) {
        return gamesMap.get(gameId);
    }

    /* TODO: move to converter */
    @CheckForNull
    private GamePlayerDto convertPlayer(@CheckForNull final GamePlayerDto player) {
        if (player == null) {
            return null;
        }
        return GamePlayerDto.builder()
                .playerId(player.getPlayerId())
                .name(player.getName())
                .build();
    }

    private GameDto convertToDto(final String gameId) {
        final var game = gamesMap.get(gameId);
        final var player1 = playerService.getById(game.getPlayer1Id());
        final var player2 = playerService.getById(game.getPlayer2Id());
        return toDto(game, player1, player2);
    }
}
