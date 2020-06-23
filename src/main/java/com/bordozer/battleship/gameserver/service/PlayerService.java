package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.GamePlayerDto;

public interface PlayerService {

    GamePlayerDto getById(String playerId);
}
