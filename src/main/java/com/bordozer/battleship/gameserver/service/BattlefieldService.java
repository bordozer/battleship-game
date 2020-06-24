package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.PlayerMove;

public interface BattlefieldService {

    void move(Battlefield battlefield, PlayerMove move);
}
