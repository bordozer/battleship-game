package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.converter.ShipConverter;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.model.Ship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CellUtils {

    public static BattlefieldCell getCell(final List<List<BattlefieldCell>> cells, final PlayerMove move) {
        return cells.stream()
                .flatMap(Collection::stream)
                .filter(cell -> cell.getLine() == move.getLine() && cell.getColumn() == move.getColumn())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Player's move cannot be found on battlefield: %s", move)));
    }

    public static List<Ship> collectShips(final ArrayList<ArrayList<CellDto>> cells) {
        return cells.stream()
                .flatMap(Collection::stream)
                .map(CellDto::getShip)
                .filter(Objects::nonNull)
                .distinct()
                .map(ShipConverter::convertToShip)
                .collect(Collectors.toList());
    }

    public static List<Ship> collectShips(final List<List<BattlefieldCell>> cells) {
        return cells.stream()
                .flatMap(Collection::stream)
                .map(BattlefieldCell::getShip)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
