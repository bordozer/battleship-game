package com.bordozer.battleship.gameserver.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public abstract class Battle {
    private final Game game;
    private final Battlefield battlefield1 = new Battlefield();
    private final Battlefield battlefield2 = new Battlefield();
}
