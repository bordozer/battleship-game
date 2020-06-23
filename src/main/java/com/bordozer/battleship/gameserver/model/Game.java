package com.bordozer.battleship.gameserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.CheckForNull;

@Getter
@Setter
@Builder
@ToString
public class Game {
    @NonNull
    private final String gameId;
    @NonNull
    private GameState state;
    @NonNull
    private final String player1;
    @CheckForNull
    private String player2;
    @CheckForNull
    private String winner;
}
