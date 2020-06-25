package com.bordozer.battleship.gameserver.dto.battle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import javax.annotation.CheckForNull;

@JsonDeserialize(
        builder = ImmutableGameplayDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class GameplayDto {

    public abstract String getGameId();

    public abstract GameStep getStep();

    @CheckForNull
    public abstract PlayerType getCurrentMove();

    @CheckForNull
    public abstract String getWinner();

    public static ImmutableGameplayDto.Builder builder() {
        return ImmutableGameplayDto.builder();
    }
}
