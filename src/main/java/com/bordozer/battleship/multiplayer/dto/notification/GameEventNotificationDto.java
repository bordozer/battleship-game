package com.bordozer.battleship.multiplayer.dto.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

@JsonDeserialize(
        builder = ImmutableGameEventNotificationDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class GameEventNotificationDto implements Notifiable {

    public static ImmutableGameEventNotificationDto.Builder builder() {
        return ImmutableGameEventNotificationDto.builder();
    }
}
