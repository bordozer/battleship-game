package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.dto.GamePlayerDto;

public interface PlayerService {

    GamePlayerDto getById(String playerId);
}
