package com.bordozer.battleship.multiplayer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class Game {
    @NonNull
    private final String gameId;
    @NonNull
    private final LocalDateTime created;
    @NonNull
    private GameState state;
    @NonNull
    private final String player1Id;
    @CheckForNull
    private String player2Id;
    @NonNull
    private final Battle battle;
    @CheckForNull
    private String winnerId;

    public Battlefield getBattlefieldFor(final String playerId) {
        return playerId.equals(player1Id) ? battle.getBattlefield1() : battle.getBattlefield2();
    }
}
