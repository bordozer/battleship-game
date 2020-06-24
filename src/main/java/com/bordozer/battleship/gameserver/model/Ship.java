package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class Ship {
    private final String shipId;
//    private final List<ShipCell> shipCells;
}
