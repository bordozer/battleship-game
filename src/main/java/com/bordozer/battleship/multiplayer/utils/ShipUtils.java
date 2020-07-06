package com.bordozer.battleship.multiplayer.utils;

import com.bordozer.battleship.multiplayer.converter.ShipConverter;
import com.bordozer.battleship.multiplayer.dto.battle.ShipDto;
import com.bordozer.battleship.multiplayer.model.BattlefieldCell;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ShipUtils {

    public static List<ShipDto> extractShips(final List<List<BattlefieldCell>> cells) {
        return cells.stream()
                .flatMap(Collection::stream)
                .map(BattlefieldCell::getShip)
                .filter(Objects::nonNull)
                .distinct()
                .map(ShipConverter::convertToShipDto)
                .collect(Collectors.toList());
    }

    public static List<BattlefieldCell> getShipCells(final String shipId, final List<List<BattlefieldCell>> cells) {
        return cells.stream()
                .flatMap(Collection::stream)
                .filter(cell -> Optional
                        .ofNullable(cell.getShip())
                        .map(s -> shipId.equals(s.getShipId()))
                        .orElse(false)
                )
                .collect(Collectors.toList());
    }
}
