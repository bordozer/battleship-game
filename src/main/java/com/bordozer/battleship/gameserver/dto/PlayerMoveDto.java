package com.bordozer.battleship.gameserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

@JsonDeserialize(
        builder = ImmutablePlayerMoveDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class PlayerMoveDto {

    public abstract String getGameId();

    public abstract String getPlayerId();

    public abstract int getLine();

    public abstract int getColumn();
}
