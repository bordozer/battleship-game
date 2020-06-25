package com.bordozer.battleship.gameserver.converter;

import com.bordozer.battleship.gameserver.dto.battle.ImmutableShipDto;
import com.bordozer.battleship.gameserver.dto.battle.ShipDto;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.Ship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.CheckForNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShipConverter {

    public static Ship convertToShip(final ShipDto ship) {
        return Ship.builder()
                .shipId(ship.getId())
                .name(ship.getName())
                .size(ship.getSize())
                .damage(ship.getDamage())
                .build();
    }

    public static List<? extends ShipDto> convertShips(final List<List<BattlefieldCell>> cells) {
        return cells.stream()
                .flatMap(Collection::stream)
                .map(BattlefieldCell::getShip)
                .filter(Objects::nonNull)
                .distinct()
                .map(ShipConverter::convertToShipDto)
                .collect(Collectors.toList());
    }

    public static ImmutableShipDto convertToShipDto(final Ship ship) {
        return ShipDto.builder()
                .id(ship.getShipId())
                .name(ship.getName())
                .size(ship.getSize())
                .damage(ship.getDamage())
                .cells(Collections.emptyList())
                .build();
    }

    @CheckForNull
    public static ShipDto convertShip(@CheckForNull final Ship ship) {
        return Optional.ofNullable(ship)
                .map(s -> ShipDto.builder()
                        .id(s.getShipId())
                        .name(s.getName())
                        .size(s.getSize())
                        .damage(s.getDamage())
                        .build())
                .orElse(null);
    }
}
