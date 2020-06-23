package com.bordozer.battleship.gameserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Cell {
    private final int line;
    private final int column;
    private final String xLabel;
    private final String yLabel;

    public String getId() {
        return String.format("%s+%s", line, column);
    }
}
