package com.bordozer.battleship.gameserver.dto.battle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import java.time.LocalDateTime;

@JsonDeserialize(
        builder = ImmutableLogDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class LogDto {

    public abstract LocalDateTime getTime();

    public abstract String getText();
}
