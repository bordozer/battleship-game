package com.bordozer.battleship.multiplayer.controller;

import com.bordozer.battleship.multiplayer.dto.battle.BattleDto;
import com.bordozer.battleship.multiplayer.service.BattleService;
import com.bordozer.battleship.multiplayer.service.RequestHelper;
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

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/battle")
public class BattleController {

    private final RequestHelper requestHelper;
    private final BattleService battleService;

    @GetMapping(path = "{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BattleDto> games(@PathVariable(value = "gameId") final String gameId, final HttpServletRequest request) {
        final var playerId = requestHelper.getPlayerId(request);
        LOGGER.info("Player \"{}\" requested game \"{}\" state", playerId, gameId);
        return new ResponseEntity<>(battleService.getGameState(gameId, playerId), HttpStatus.OK);
    }
}
