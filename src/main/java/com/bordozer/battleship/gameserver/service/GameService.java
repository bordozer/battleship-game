package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.GameDto;

public interface GameService {

    GameDto create(final String player1Id);

    GameDto joinGame(final String gameId, final String player2Id);
}
