package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.dto.GameDto;
import com.bordozer.battleship.multiplayer.dto.battle.CellDto;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.GamePlayers;

import java.util.ArrayList;
import java.util.List;

public interface GameService {

    List<GameDto> getGames(final String playerId);

    GameDto create(String player1Id, final ArrayList<ArrayList<CellDto>> cells);

    void joinGame(String gameId, String player2Id, final ArrayList<ArrayList<CellDto>> cells);

    Game getGame(String gameId);

    GamePlayers getGamePlayers(String gameId);

    void delete(String gameId, final String playerId);
}