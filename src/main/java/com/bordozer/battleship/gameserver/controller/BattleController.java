package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.service.BattleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/battle")
public class BattleController {

    private final BattleService battleService;

    @GetMapping(path = "/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BattleDto> games(@PathVariable("gameId") final String gameId) {
        return new ResponseEntity<>(battleService.getBattle(gameId), HttpStatus.OK);
    }
}
