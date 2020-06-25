package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.model.PlayerMove;

public interface BattleService {

    BattleDto getGameState(String gameId, final String forPlayerId);

    void move(String gameId, String playerId, PlayerMove move);
}
