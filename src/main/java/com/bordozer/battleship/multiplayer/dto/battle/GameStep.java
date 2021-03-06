package com.bordozer.battleship.multiplayer.dto.battle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum GameStep {
    @JsonProperty("WAITING_FOR_OPPONENT")
    WAITING_FOR_OPPONENT,
    @JsonProperty("BATTLE")
    BATTLE,
    @JsonProperty("FINISHED")
    FINISHED,
    @JsonProperty("CANCELLED")
    CANCELLED;
}
