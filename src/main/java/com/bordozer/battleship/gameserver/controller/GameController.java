package com.bordozer.battleship.gameserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/games")
@CrossOrigin("*")
public class GameController {

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> games() {
        return new ResponseEntity<>(newArrayList("Game 1", "Game 2"), HttpStatus.OK);
    }

    @GetMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createNewGame() {
        return new ResponseEntity<>("gameId", HttpStatus.OK);
    }

    @GetMapping(path = "/join/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> joinGame(@PathVariable("gameId") final String gameId) {
        return new ResponseEntity<>("gameId", HttpStatus.OK);
    }
}
