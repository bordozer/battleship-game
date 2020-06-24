package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.battle.BattleDto;

public interface BattleService {

    BattleDto getBattle(String gameId);
}
