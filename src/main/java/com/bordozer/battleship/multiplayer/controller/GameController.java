package com.bordozer.battleship.multiplayer.controller;

import com.bordozer.battleship.multiplayer.dto.GameDto;
import com.bordozer.battleship.multiplayer.dto.GamesForPlayerDto;
import com.bordozer.battleship.multiplayer.dto.battle.CellDto;
import com.bordozer.battleship.multiplayer.service.GameService;
import com.bordozer.battleship.multiplayer.service.RequestHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/games")
public class GameController {

    private final RequestHelper requestHelper;
    private final GameService gameService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GamesForPlayerDto> games(final HttpServletRequest request) {
        final var playerId = requestHelper.getPlayerId(request);
        LOGGER.info("Player \"{}\" listed games", playerId);
        final var result = GamesForPlayerDto.builder()
                .playerGames(gameService.getPlayerGames(playerId))
                .openGames(gameService.getOpenGames(playerId))
                .build();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDto> createNewGame(@RequestBody final ArrayList<ArrayList<CellDto>> cells,
                                                 final HttpServletRequest request) {
        final var playerId = requestHelper.getPlayerId(request);
        LOGGER.info("Player \"{}\" created a game", playerId);
        final var game = gameService.createGame(playerId, cells);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PutMapping(path = "/join/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ok> joinGame(@PathVariable("gameId") final String gameId,
                                       @RequestBody final ArrayList<ArrayList<CellDto>> cells,
                                       final HttpServletRequest request) {
        final var playerId = requestHelper.getPlayerId(request);
        LOGGER.info("Player \"{}\" joined a game \"{}\"", playerId, gameId);
        gameService.joinGame(gameId, playerId, cells);
        return new ResponseEntity<>(Ok.of(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/cancel/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ok> cancelGame(@PathVariable("gameId") final String gameId, final HttpServletRequest request) {
        final var playerId = requestHelper.getPlayerId(request);
        LOGGER.info("Player \"{}\" cancelled a game \"{}\"", playerId, gameId);
        gameService.cancelGame(gameId, playerId);
        return new ResponseEntity<>(Ok.of(), HttpStatus.OK);
    }

    @Getter
    private static class Ok {
        private final String status = "OK";

        public static Ok of() {
            return new Ok();
        }
    }
}
