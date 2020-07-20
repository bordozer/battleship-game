package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.service.IdentityService;
import com.bordozer.battleship.multiplayer.service.RequestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestHelperImpl implements RequestHelper {

    private static final String PLAYER_ID = "PLAYER_ID";

    private final IdentityService identityService;

    @Override
    public String getPlayerId(final HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .map(cookies -> Arrays.stream(cookies)
                        .filter(cook -> cook.getName().equals(PLAYER_ID))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElseGet(this::generateNew)
                ).orElseGet(this::generateNew);
    }

    @Override
    public void addPlayerCookies(final HttpServletResponse response, final String playerId) {
        final var cookie = new Cookie(PLAYER_ID, playerId);
        response.addCookie(cookie);
    }

    private String generateNew() {
        return identityService.generateForUser();
    }
}
