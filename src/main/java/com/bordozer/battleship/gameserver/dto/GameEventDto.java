package com.bordozer.battleship.gameserver.dto;

import com.bordozer.battleship.gameserver.model.GameEventType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
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

    public abstract GameEventType getEventType();
}
