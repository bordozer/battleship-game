package com.bordozer.battleship.gameserver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

@SuppressWarnings("checkstyle:magicnumber")
@JsonDeserialize(
        builder = ImmutableBattleDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class BattleDto {

    public abstract String getGameId();

    public abstract PlayerMoveDto getPlayerMove();
}
