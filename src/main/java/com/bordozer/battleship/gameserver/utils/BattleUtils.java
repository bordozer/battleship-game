package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.dto.battle.ShipDto;
import com.bordozer.battleship.gameserver.model.Battle;
import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.Cell;
import com.bordozer.battleship.gameserver.model.LogItem;
import com.bordozer.battleship.gameserver.model.Ship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BattleUtils {

    private static final int BATTLEFIELD_SIZE = 10;

    public static Battle initBattle(final ArrayList<ArrayList<CellDto>> cells) {
        return Battle.builder()
                .battlefield1(new Battlefield(convertCells(cells)))
                .battlefield2(new Battlefield(emptyCells()))
                .currentMove(null)
                .logs(newArrayList(LogItem.builder().text("Game has been created").build()))
                .build();
    }

    /* TODO: move to converter */
    public static List<List<BattlefieldCell>> convertCells(final ArrayList<ArrayList<CellDto>> playerCells) {
        final var ships = collectShips(playerCells);
        final List<List<BattlefieldCell>> columns = new ArrayList<>();
        for (int column = 0; column < BATTLEFIELD_SIZE; column++) {
            final var lines = new ArrayList<BattlefieldCell>();
            final var playerLines = playerCells.get(column);
            for (int line = 0; line < 10; line++) {
                final BattlefieldCell cell = initCell(column, line);

                final var playerCell = playerLines.get(line);
                final var playerShip = playerCell.getShip();
                if (playerShip != null) {
                    final var ship = getShip(playerShip.getId(), ships);
                    cell.setShip(ship);
                }
                lines.add(cell);
            }
            columns.add(lines);
        }
        return columns;
    }

    public static String toColumn(final int x) {
        return Cell.X_AXE[x];
    }

    public static List<Ship> collectShips(final ArrayList<ArrayList<CellDto>> cells) {
        return cells.stream()
                .flatMap(Collection::stream)
                .map(CellDto::getShip)
                .filter(Objects::nonNull)
                .distinct()
                .map(BattleUtils::convertToShip)
                .collect(Collectors.toList());
    }

    private static Ship getShip(final String shipId, final List<Ship> ships) {
        return ships.stream()
                .filter(s -> s.getShipId().equals(shipId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("shipId %s not found", shipId)));
    }

    private static Ship convertToShip(final ShipDto ship) {
        return Ship.builder()
                .shipId(ship.getId())
                .name(ship.getName())
                .size(ship.getSize())
                .damage(ship.getDamage())
                .build();
    }

    private static List<List<BattlefieldCell>> emptyCells() {
        final List<List<BattlefieldCell>> cells = new ArrayList<>();
        for (int column = 0; column < 10; column++) {
            final var lines = new ArrayList<BattlefieldCell>();
            for (int line = 0; line < 10; line++) {
                lines.add(initCell(column, line));
            }
            cells.add(lines);
        }
        return cells;
    }

    private static BattlefieldCell initCell(final int column, final int line) {
        return new BattlefieldCell(line, column);
    }
}
