package com.bordozer.battleship.gameserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Battlefield {
    private List<List<BattlefieldCell>> cells;
}
