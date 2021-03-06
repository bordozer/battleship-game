package com.bordozer.battleship.multiplayer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

@JsonDeserialize(
        builder = ImmutableGameEventDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class GameEventDto {

    public abstract String getGameId();

    public abstract String getPlayerId();

    public abstract EventType getEventType();
}
