package com.bordozer.battleship.gameserver.exception;

import lombok.Getter;

@Getter
public class GameNotFoundException extends RuntimeException {

    private final String gameId;

    public GameNotFoundException(final String gameId) {
        super(String.format("Game '%s' not found", gameId));
        this.gameId = gameId;
    }
}
