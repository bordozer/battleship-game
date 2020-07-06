package com.bordozer.battleship.multiplayer.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.CheckForNull;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GamePlayers {
    private final String player1Id;
    @CheckForNull
    private final String player12d;

    public static GamePlayers of(final String player1Id, @CheckForNull final String player2Id) {
        return new GamePlayers(player1Id, player2Id);
    }
}
