package com.bordozer.battleship.gameserver.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestUtils {

    private static final String PLAYER_ID = "PLAYER_ID";

    public static String getPlayerId(final HttpServletRequest request) {
        return Optional.ofNullable(request.getCookies())
                .map(cookies -> Arrays.stream(cookies)
                        .filter(cook -> cook.getName().equals(PLAYER_ID))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(UUID.randomUUID().toString())
                ).orElse(UUID.randomUUID().toString());
    }

    public static void addPlayerCookies(final HttpServletResponse response, final String playerId) {
        final var cookie = new Cookie(PLAYER_ID, playerId);
        response.addCookie(cookie);
    }
}
