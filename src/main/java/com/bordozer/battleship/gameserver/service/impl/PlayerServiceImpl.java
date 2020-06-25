package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GamePlayerDto;
import com.bordozer.battleship.gameserver.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
