package com.bordozer.battleship.multiplayer.utils;

import com.bordozer.battleship.multiplayer.converter.CellConverter;
import com.bordozer.battleship.multiplayer.converter.ShipConverter;
import com.bordozer.battleship.multiplayer.dto.battle.CellDto;
import com.bordozer.battleship.multiplayer.model.BattlefieldCell;
import com.bordozer.battleship.multiplayer.model.PlayerMove;
import com.bordozer.battleship.multiplayer.model.Ship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bordozer.battleship.multiplayer.utils.ShipUtils.getShipCells;

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

    public static List<List<BattlefieldCell>> initCells() {
        final List<List<BattlefieldCell>> cells = new ArrayList<>();
        for (int column = 0; column < 10; column++) {
            final var lines = new ArrayList<BattlefieldCell>();
            for (int line = 0; line < 10; line++) {
                lines.add(CellConverter.initCell(column, line));
            }
            cells.add(lines);
        }
        return cells;
    }

    public static CellDto getCell(final int x, final int y, final List<List<CellDto>> cells) {
        return cells.get(y).get(x);
    }

    public static List<BattlefieldCell> getShipNeighbourCells(final String shipId, final List<List<BattlefieldCell>> cells, final int battlefieldSize) {
        return getShipCells(shipId, cells).stream()
                .map(cell -> getNeighbourCells(cell, cells, battlefieldSize))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static List<BattlefieldCell> getNeighbourCells(final BattlefieldCell cell, final List<List<BattlefieldCell>> cells, final int battlefieldSize) {
        final var result = new ArrayList<BattlefieldCell>();

        final var cellY = cell.getColumn();
        final var cellX = cell.getLine();
        for (int y = cellY - 1; y <= cellY + 1; y++) {
            for (int x = cellX - 1; x <= cellX + 1; x++) {
                if (x < 0) {
                    continue;
                }
                if (x > battlefieldSize - 1) {
                    continue;
                }
                if (y < 0) {
                    continue;
                }
                if (y > battlefieldSize - 1) {
                    continue;
                }
                final var columns = cells.get(y);
                final var aCell = columns.get(x);
                if (aCell.getId().equals(cell.getId())) {
                    continue;
                }
                result.add(aCell);
            }
        }
        return result;
    }
}
