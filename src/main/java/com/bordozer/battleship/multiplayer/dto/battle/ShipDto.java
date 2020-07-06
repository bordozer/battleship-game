package com.bordozer.battleship.multiplayer.dto.battle;

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

    public abstract String getId();

    public abstract String getName();

    public abstract int getSize();

    public abstract int getDamage();

    public abstract List<CellDto> getCells();

    public static ImmutableShipDto.Builder builder() {
        return ImmutableShipDto.builder();
    }
}
