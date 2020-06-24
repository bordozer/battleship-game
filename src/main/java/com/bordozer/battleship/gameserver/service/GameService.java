package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.model.Game;

import java.util.ArrayList;
import java.util.List;

public interface GameService {

    List<GameDto> getOpenGames();

    GameDto create(String player1Id, final ArrayList<ArrayList<CellDto>> cells);

    void joinGame(String gameId, String player2Id);

    Game getGame(String gameId);
}
