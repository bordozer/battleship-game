package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.GameDto;

import java.util.List;

public interface GameService {

    List<GameDto> getOpenGames();

    GameDto create(String player1Id);

    GameDto joinGame(String gameId, String player2Id);

    GameDto getGame(String gameId);
}
