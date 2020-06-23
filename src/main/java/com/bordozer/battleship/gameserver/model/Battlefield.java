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
public class Battlefield {
    private final List<List<BattlefieldCell>> cells;
}
