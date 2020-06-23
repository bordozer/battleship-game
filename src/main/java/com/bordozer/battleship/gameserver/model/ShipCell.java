package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShipCell extends Cell{
    private boolean damaged;

    public ShipCell(final String line, final String column) {
        super(line, column);
    }
}
