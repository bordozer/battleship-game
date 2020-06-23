package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.CheckForNull;

@Getter
@Setter
@ToString
public class BattlefieldCell extends Cell {
    @CheckForNull
    private Ship ship;
    private boolean hit;
    private boolean shipNeighbor;
    private boolean killedShipNeighbor;

    public BattlefieldCell(final String line, final String column) {
        super(line, column);
    }
}
