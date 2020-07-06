package com.bordozer.battleship.multiplayer.model;

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

    public BattlefieldCell(final int line, final int column) {
        super(line, column);
    }
}
