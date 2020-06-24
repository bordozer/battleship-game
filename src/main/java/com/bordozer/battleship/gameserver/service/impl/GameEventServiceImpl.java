package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.GameEventDto;
import com.bordozer.battleship.gameserver.service.GameEventService;
import com.bordozer.battleship.gameserver.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameEventServiceImpl implements GameEventService {

    private final GameService gameService;

    @Override
    public void process(final GameEventDto event) {
        switch (event.getEventType()) {
            case JOIN_GAME:
                break;
            default:
                throw new IllegalArgumentException(String.format("Unsuppurted event type: '%s'", event.getEventType()));
        }
    }
}
