package com.bordozer.battleship.gameserver.dto.battle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrentMove {
    PLAYER1("player"),
    PLAYER2("enemy");

    private final String value;
}
