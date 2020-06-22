package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.PlayerDto;
import com.bordozer.battleship.gameserver.service.PlayerService;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Override
    public PlayerDto getById(final String playerId) {
        return PlayerDto.builder()
                .id(playerId)
                .build();
    }
}
