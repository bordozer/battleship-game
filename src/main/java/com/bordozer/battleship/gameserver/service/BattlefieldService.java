package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.PlayerMove;

public interface BattlefieldService {

    void move(final Game game, Battlefield battlefield, final String playerId, PlayerMove move);
}
