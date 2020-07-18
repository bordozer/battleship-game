package com.bordozer.battleship.multiplayer.exception;

import lombok.Getter;

@Getter
public class IncopatibleGameStatusException extends RuntimeException {

    private final String gameId;

    public IncopatibleGameStatusException(final String gameId) {
        super(String.format("Game '%s' has wrong status", gameId));
        this.gameId = gameId;
    }
}
