package com.bordozer.battleship.gameserver.dto.battle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrentMove {
    @JsonProperty("player")
    PLAYER1("player"),

    @JsonProperty("enemy")
    PLAYER2("enemy");

    private final String value;
}
