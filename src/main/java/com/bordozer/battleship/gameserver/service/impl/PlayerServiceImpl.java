package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GamePlayerDto;
import com.bordozer.battleship.gameserver.service.PlayerService;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Override
    public GamePlayerDto getById(final String playerId) {
        return GamePlayerDto.builder()
                .playerId(playerId)
                .name("Fake User")
                .build();
    }
}
