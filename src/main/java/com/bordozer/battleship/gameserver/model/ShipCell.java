package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShipCell extends Cell{
    private boolean damaged;

    public ShipCell(final int line, final int column, final String xLabel, final String yLabel) {
        super(line, column, xLabel, yLabel);
    }
}
