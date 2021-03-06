package com.bordozer.battleship.multiplayer.dto;

import com.bordozer.battleship.multiplayer.dto.battle.GameStep;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import javax.annotation.CheckForNull;

@JsonDeserialize(
        builder = ImmutableGameDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class GameDto {

    public abstract String getGameId();

    public abstract GamePlayerDto getPlayer1();

    public abstract String getCreated();

    @CheckForNull
    public abstract GamePlayerDto getPlayer2();

    public abstract GameStep getGameStep();

    public static ImmutableGameDto.Builder builder() {
        return ImmutableGameDto.builder();
    }
}
