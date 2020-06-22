package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.PlayerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static com.bordozer.battleship.gameserver.utils.RequestUtils.getPlayerId;
import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/games")
//@CrossOrigin("*")
public class GameController {

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDto>> games() {
        return new ResponseEntity<>(newArrayList(), HttpStatus.OK);
    }

    @GetMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> createNewGame(final HttpServletRequest request) {
        final var playerId = getPlayerId(request);
        final var player = PlayerDto.builder()
                .id(playerId)
                .build();
        final var game = GameDto.builder()
                .gameId(UUID.randomUUID().toString())
                .player1(player)
                .build();
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping(path = "/join/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> joinGame(@PathVariable("gameId") final String gameId, final HttpServletRequest request) {
        final var playerId = getPlayerId(request);
        final var player = PlayerDto.builder()
                .id(playerId)
                .build();
        final var game = GameDto.builder()
                .gameId(gameId)
                .player2(player)
                .build();
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
