package com.bordozer.battleship.gameserver.dto.battle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import java.util.List;

@JsonDeserialize(
        builder = ImmutableBattleDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class BattleDto {

    public abstract String getGameId();

    public abstract PlayerDto getPlayer();

    public abstract PlayerDto getEnemy();

    public abstract GameConfigDto getConfig();

    public abstract GameplayDto getGameplay();

    public abstract List<LogDto> getLogs();

    public static ImmutableBattleDto.Builder builder() {
        return ImmutableBattleDto.builder();
    }
}
