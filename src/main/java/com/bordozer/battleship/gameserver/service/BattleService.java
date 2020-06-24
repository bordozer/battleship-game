package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.battle.BattleDto;

public interface BattleService {

    void initBattle(String gameId);

    BattleDto getBattle(String gameId);
}
