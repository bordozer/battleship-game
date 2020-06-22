package com.bordozer.battleship.gameserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Game {
    @NonNull
    private final String gameId;
    @NonNull
    private final String player1;
    private String player2;
    @NonNull
    private GameState state;
}
