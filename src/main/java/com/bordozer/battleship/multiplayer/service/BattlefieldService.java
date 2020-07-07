package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.model.Battlefield;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.LogItem;
import com.bordozer.battleship.multiplayer.model.PlayerMove;

import java.util.List;

public interface BattlefieldService {

    List<LogItem> move(final Game game, Battlefield battlefield, final String playerId, PlayerMove move);
}
