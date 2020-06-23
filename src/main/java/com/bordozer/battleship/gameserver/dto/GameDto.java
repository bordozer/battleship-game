package com.bordozer.battleship.gameserver.dto;

import com.bordozer.battleship.gameserver.dto.battle.PlayerDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

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

    public abstract GamePlayerDto getPlayer2();

    public static ImmutableGameDto.Builder builder() {
        return ImmutableGameDto.builder();
    }
}
