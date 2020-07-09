package com.bordozer.battleship.multiplayer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import java.util.List;

@JsonDeserialize(
        builder = ImmutableGameNotificationDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class GameNotificationDto {

    public abstract String getGameId();

    public abstract String getPlayerId();

    public abstract EventType getEventType();

    public abstract List<String> getMessages();

    public static ImmutableGameNotificationDto.Builder builder() {
        return ImmutableGameNotificationDto.builder();
    }
}
