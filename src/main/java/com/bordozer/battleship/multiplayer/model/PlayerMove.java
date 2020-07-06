package com.bordozer.battleship.multiplayer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class PlayerMove {
    private final int line;
    private final int column;

    public static PlayerMove of(final int line, final int column) {
        return new PlayerMove(line, column);
    }
}
