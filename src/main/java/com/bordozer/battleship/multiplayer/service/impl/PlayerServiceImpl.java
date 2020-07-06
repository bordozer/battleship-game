package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.dto.GamePlayerDto;
import com.bordozer.battleship.multiplayer.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    @Override
    public GamePlayerDto getById(final String playerId) {
        return GamePlayerDto.builder()
                .id(playerId)
                .name(String.format("User-%s", playerId.substring(playerId.length() - 4)))
                .build();
    }
}
