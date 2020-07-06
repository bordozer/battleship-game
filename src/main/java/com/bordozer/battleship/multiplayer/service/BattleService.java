package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.dto.battle.BattleDto;
import com.bordozer.battleship.multiplayer.model.PlayerMove;

public interface BattleService {

    BattleDto getGameState(String gameId, final String forPlayerId);

    void move(String gameId, String playerId, PlayerMove move);
}
