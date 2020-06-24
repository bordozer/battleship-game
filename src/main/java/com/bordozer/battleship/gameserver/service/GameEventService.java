package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.GameEventDto;

public interface GameEventService {

    void process(GameEventDto event);
}
