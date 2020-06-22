package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.ImmutableGameDto;
import com.bordozer.battleship.gameserver.service.GameService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final Map<String, GameDto> gamesMap = new ConcurrentHashMap<>();

    private final PlayerService playerService;

    @Override
    public GameDto create(final String playerId) {
        final var player = playerService.getById(playerId);

        final var gameId = UUID.randomUUID().toString();
        final var game = GameDto.builder()
                .gameId(gameId)
                .player1(player)
                .build();

        gamesMap.put(gameId, game);

        return game;
    }

    @Override
    public GameDto joinGame(final String gameId, final String playerId) {
        final var player = playerService.getById(playerId);
        final var game = gamesMap.get(gameId);
        return ImmutableGameDto.copyOf(game)
                .withPlayer2(player);
    }
}
