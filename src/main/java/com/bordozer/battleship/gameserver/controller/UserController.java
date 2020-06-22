package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.ImmutableWhoAmIDto;
import com.bordozer.battleship.gameserver.dto.WhoAmIDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.CheckForNull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/whoami")
public class UserController {

    private static final String PLAYER_ID = "PLAYER_ID";

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WhoAmIDto> games(final HttpServletRequest request, final HttpServletResponse response) {
        final var playerId = Arrays.stream(request.getCookies())
                .filter(cook -> cook.getName().equals(PLAYER_ID))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(UUID.randomUUID().toString());

        final var cookie = new Cookie(PLAYER_ID, playerId);
        response.addCookie(cookie);

        final var whoAmIDto = ImmutableWhoAmIDto.builder()
                .playerId(playerId)
                .build();
        return new ResponseEntity<>(whoAmIDto, HttpStatus.OK);
    }
}
