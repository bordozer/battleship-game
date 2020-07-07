package com.bordozer.battleship.multiplayer.dto.notification;

import com.bordozer.battleship.multiplayer.model.LogItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import java.util.List;

@JsonDeserialize(
        builder = ImmutablePlayerMoveNotificationDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class PlayerMoveNotificationDto implements Notifiable {

    public abstract List<LogItem> getMoveLogs();

    public static ImmutablePlayerMoveNotificationDto.Builder builder() {
        return ImmutablePlayerMoveNotificationDto.builder();
    }
}
