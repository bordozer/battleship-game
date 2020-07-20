package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.service.ClockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ClockServiceImpl implements ClockService {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
