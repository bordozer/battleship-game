package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.LogItem;
import com.bordozer.battleship.gameserver.model.PlayerMove;

import java.util.List;

public interface BattlefieldService {

    List<LogItem> move(final Game game, Battlefield battlefield, final String playerId, PlayerMove move);
}
