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
        return "8327de36-5a26-4034-a032-e7bc6b221084"; // TODO: stub, use generate()0
    }

    private static String generate() {
        return UUID.randomUUID().toString();
    }
}
