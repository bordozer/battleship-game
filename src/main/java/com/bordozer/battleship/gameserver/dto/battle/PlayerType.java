package com.bordozer.battleship.gameserver.dto.battle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum PlayerType {
    @JsonProperty("player")
    PLAYER1,
    @JsonProperty("enemy")
    PLAYER2;
}
