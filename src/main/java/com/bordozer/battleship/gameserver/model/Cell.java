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

    private static final String[] X_AXE = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "K"};

    private final int line;
    private final int column;

    public String getId() {
        return String.format("%s+%s", line, column);
    }

    public String getXLabel() {
        return X_AXE[line];
    }

    public String getYLabel() {
        return String.valueOf(column + 1);
    }

    public String humanize() {
        return String.format("%s%s", getXLabel(), getYLabel());
    }
}
