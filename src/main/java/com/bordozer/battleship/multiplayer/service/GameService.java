package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.dto.GameDto;
import com.bordozer.battleship.multiplayer.dto.battle.CellDto;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.GamePlayers;

import java.util.ArrayList;
import java.util.List;

public interface GameService {

    List<GameDto> getGames(final String playerId);

    List<GameDto> getPlayerGames(final String playerId);

    List<GameDto> getOpenGames(final String playerId);

    GameDto createGame(String player1Id, final ArrayList<ArrayList<CellDto>> cells);

    void joinGame(String gameId, String player2Id, final ArrayList<ArrayList<CellDto>> cells);

    Game getGame(String gameId);

    GamePlayers getGamePlayers(String gameId);

    void cancelGame(String gameId, final String playerId);
}
