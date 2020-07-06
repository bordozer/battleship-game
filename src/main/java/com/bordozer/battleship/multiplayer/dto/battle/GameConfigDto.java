package com.bordozer.battleship.multiplayer.dto.battle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

@JsonDeserialize(
        builder = ImmutableGameConfigDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class GameConfigDto {

    public abstract Boolean getShowShotHints();

    public abstract int getDifficulty();

    public static ImmutableGameConfigDto.Builder builder() {
        return ImmutableGameConfigDto.builder();
    }
}
