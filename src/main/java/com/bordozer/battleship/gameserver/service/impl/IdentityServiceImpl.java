package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdentityServiceImpl implements IdentityService {

    @Override
    public String generateForUser() {
        return generate();
    }

    @Override
    public String generateForGame() {
        return generate();
    }

    private static String generate() {
        return UUID.randomUUID().toString();
    }
}
