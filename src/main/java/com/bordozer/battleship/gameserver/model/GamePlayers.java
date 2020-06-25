package com.bordozer.battleship.gameserver.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GamePlayers {
    private final String player1Id;
    private final String player12d;

    public static GamePlayers of(final String player1Id, final String player2Id) {
        return new GamePlayers(player1Id, player2Id);
    }
}
