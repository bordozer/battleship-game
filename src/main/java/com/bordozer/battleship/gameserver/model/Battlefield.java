package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Battlefield {
    private final List<List<BattlefieldCell>> cells = new ArrayList<>();
}
