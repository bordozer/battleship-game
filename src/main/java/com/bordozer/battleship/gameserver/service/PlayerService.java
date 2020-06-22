package com.bordozer.battleship.gameserver.service;

import com.bordozer.battleship.gameserver.dto.PlayerDto;

public interface PlayerService {

    PlayerDto getById(String playerId);
}
