package com.bordozer.battleship.multiplayer.controller;

import com.bordozer.battleship.multiplayer.dto.ImmutableWhoAmIDto;
import com.bordozer.battleship.multiplayer.dto.WhoAmIDto;
import com.bordozer.battleship.multiplayer.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bordozer.battleship.multiplayer.utils.RequestUtils.addPlayerCookies;
import static com.bordozer.battleship.multiplayer.utils.RequestUtils.getPlayerId;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/whoami")
public class UserController {

    private final PlayerService playerService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WhoAmIDto> games(final HttpServletRequest request, final HttpServletResponse response) {
        final var playerId = getPlayerId(request);
        LOGGER.info("Player \"{}\" called whoami", playerId);
        addPlayerCookies(response, playerId);

        final var whoAmIDto = ImmutableWhoAmIDto.builder()
                .player(playerService.getById(playerId))
                .build();

        return new ResponseEntity<>(whoAmIDto, HttpStatus.OK);
    }
}
