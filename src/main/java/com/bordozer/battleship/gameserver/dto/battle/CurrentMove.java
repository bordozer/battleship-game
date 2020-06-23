package com.bordozer.battleship.gameserver.dto.battle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrentMove {
    PLAYER("player"),
    ENEMY("enemy");

    private final String value;
}
