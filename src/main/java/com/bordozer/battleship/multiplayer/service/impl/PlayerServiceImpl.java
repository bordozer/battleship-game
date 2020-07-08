package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.dto.GamePlayerDto;
import com.bordozer.battleship.multiplayer.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private static final Map<String, GamePlayerDto> PLAYERS = new ConcurrentHashMap<>();
    static {
        PLAYERS.put("ea287b6a-15d1-424c-986f-612d68f9da02", GamePlayerDto.builder()
                .id("ea287b6a-15d1-424c-986f-612d68f9da02")
                .name("Arthur Herbert")
                .build());
        PLAYERS.put("2b6d4c74-aae0-4a57-95a3-43f4cb8d9527", GamePlayerDto.builder()
                .id("2b6d4c74-aae0-4a57-95a3-43f4cb8d9527")
                .name("Edward Russell")
                .build());
        PLAYERS.put("e4e79d80-5df7-4e6a-b8ac-cfd974623685", GamePlayerDto.builder()
                .id("e4e79d80-5df7-4e6a-b8ac-cfd974623685")
                .name("Sir John Norris")
                .build());
    }

    @Override
    public GamePlayerDto getById(final String playerId) {
        return Optional.ofNullable(PLAYERS.get(playerId))
                .orElse(GamePlayerDto.builder()
                        .id(playerId)
                        .name(String.format("User-%s", playerId.substring(playerId.length() - 4)))
                        .build());
    }
}
