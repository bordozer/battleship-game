package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.model.Battlefield;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.PlayerMove;

public interface BattlefieldService {

    void move(final Game game, Battlefield battlefield, final String playerId, PlayerMove move);
}
