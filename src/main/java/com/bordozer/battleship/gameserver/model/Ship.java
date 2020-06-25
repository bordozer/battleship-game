package com.bordozer.battleship.gameserver.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(of = {"shipId"})
public class Ship {
    private final String shipId;
    private final String name;
    private final int size;
    private int damage;
    //    private final List<ShipCell> shipCells;

    public void damage() {
        damage++;
    }
}
