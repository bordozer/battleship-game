package com.bordozer.battleship.gameserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Ship {
    private final String shipId;
    private final String name;
    private final int size;
    private final int damage;
    //    private final List<ShipCell> shipCells;
}
