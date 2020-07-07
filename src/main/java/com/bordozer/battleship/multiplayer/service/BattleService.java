package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.dto.battle.BattleDto;
import com.bordozer.battleship.multiplayer.model.LogItem;
import com.bordozer.battleship.multiplayer.model.PlayerMove;

import java.util.List;

public interface BattleService {

    BattleDto getGameState(String gameId, final String forPlayerId);

    List<LogItem> move(String gameId, String playerId, PlayerMove move);
}
