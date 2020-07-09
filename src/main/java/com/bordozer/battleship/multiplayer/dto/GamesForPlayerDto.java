package com.bordozer.battleship.multiplayer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import java.util.List;

@JsonDeserialize(
        builder = ImmutableGamesForPlayerDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class GamesForPlayerDto {

    public abstract List<GameDto> getPlayerGames();

    public abstract List<GameDto> getOpenGames();

    public static ImmutableGamesForPlayerDto.Builder builder() {
        return ImmutableGamesForPlayerDto.builder();
    }
}
