package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.bordozer.battleship.gameserver.utils.RequestUtils.getPlayerId;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/games")
//@CrossOrigin("*")
public class GameController {

    private final GameService gameService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GameDto>> games() {
        return new ResponseEntity<>(gameService.getOpenGames(), HttpStatus.OK);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> createNewGame(final HttpServletRequest request) {
        final var playerId = getPlayerId(request);
        final var game = gameService.create(playerId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PutMapping(path = "/join/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> joinGame(@PathVariable("gameId") final String gameId, final HttpServletRequest request) {
        final var playerId = getPlayerId(request);
        final var game = gameService.joinGame(gameId, playerId);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }
}
