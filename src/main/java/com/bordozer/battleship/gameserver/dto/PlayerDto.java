package com.bordozer.battleship.gameserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

@JsonDeserialize(
        builder = ImmutablePlayerDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class PlayerDto {

    public abstract String getId();

    public abstract String getName();

    public static ImmutablePlayerDto.Builder builder() {
        return ImmutablePlayerDto.builder();
    }
}
