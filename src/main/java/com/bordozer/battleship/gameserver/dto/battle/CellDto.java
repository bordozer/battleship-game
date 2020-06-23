package com.bordozer.battleship.gameserver.dto.battle;

import com.bordozer.battleship.gameserver.dto.ImmutableCellDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

@JsonDeserialize(
        builder = ImmutableCellDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class CellDto {

    public abstract int getX();

    public abstract int getY();

    public abstract String getXLabel();

    public abstract String getYLabel();

    public abstract ShipDto getShip();

    public abstract Boolean getIsHit();

    public abstract Boolean getIsShipNeighbor();

    public abstract Boolean getIsKilledShipNeighborCell();

    public String getId() {
        return String.format("%s%s", getX(), getX());
    }
}
