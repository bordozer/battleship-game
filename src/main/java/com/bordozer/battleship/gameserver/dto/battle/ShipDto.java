package com.bordozer.battleship.gameserver.dto.battle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import java.util.List;

@JsonDeserialize(
        builder = ImmutableShipDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class ShipDto {

    public abstract int getId();

    public abstract String getName();

    public abstract int getSize();

    public abstract int getDamage();

    public abstract List<CellDto> getCells();

}
