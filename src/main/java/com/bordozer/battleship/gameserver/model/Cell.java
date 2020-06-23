package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Cell {
    private final String line;
    private final String column;

    public String getId() {
        return String.format("%s+%s", line, column);
    }
}
