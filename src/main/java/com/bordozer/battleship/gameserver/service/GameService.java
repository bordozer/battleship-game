package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.model.Game;

import java.util.List;

public interface GameService {

    List<GameDto> getOpenGames();

    GameDto create(String player1Id);

    void joinGame(String gameId, String player2Id);

    Game getGame(String gameId);
}
