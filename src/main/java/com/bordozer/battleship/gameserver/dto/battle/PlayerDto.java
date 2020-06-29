package com.bordozer.battleship.gameserver.dto.battle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.ToString;
import org.immutables.value.Value;

import javax.annotation.CheckForNull;
import java.util.List;

@JsonDeserialize(
        builder = ImmutablePlayerDto.Builder.class
)
@JsonIgnoreProperties("initialized")
@Value.Immutable
@Value.Modifiable
@ToString
public abstract class PlayerDto {

    public abstract String getPlayerId();

    public abstract String getPlayerName();

    public abstract List<List<CellDto>> getCells();

    public abstract List<ShipDto> getShips();

    @CheckForNull
    public abstract CellDto getLastShot();

    public abstract List<CellDto> getDamagedShipCells();

    public static ImmutablePlayerDto.Builder builder() {
        return ImmutablePlayerDto.builder();
    }
}
