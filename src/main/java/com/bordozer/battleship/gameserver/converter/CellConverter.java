package com.bordozer.battleship.gameserver.converter;

import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.dto.battle.ShipDto;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.Ship;
import com.bordozer.battleship.gameserver.utils.BattleUtils;
import com.bordozer.battleship.gameserver.utils.CellUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CellConverter {

    public static List<List<BattlefieldCell>> convertCells(final ArrayList<ArrayList<CellDto>> playerCells) {
        final var ships = CellUtils.collectShips(playerCells);
        final List<List<BattlefieldCell>> columns = new ArrayList<>();
        for (int column = 0; column < BattleUtils.BATTLEFIELD_SIZE; column++) {
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

    private static Ship getShip(final String shipId, final List<Ship> ships) {
        return ships.stream()
                .filter(s -> s.getShipId().equals(shipId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("shipId %s not found", shipId)));
    }

    public static BattlefieldCell initCell(final int column, final int line) {
        return new BattlefieldCell(line, column);
    }

    public static List<CellDto> convertCellsToDto(final List<BattlefieldCell> columns, final boolean showShips) {
        return columns.stream()
                .map(cell -> convertCellToDto(cell, showShips))
                .collect(Collectors.toList());
    }

    private static CellDto convertCellToDto(final BattlefieldCell cell, final boolean showShips) {
        return CellDto.builder()
                .x(cell.getColumn())
                .y(cell.getLine())
                .xLabel(cell.getXLabel())
                .yLabel(cell.getYLabel())
                .isHit(Boolean.TRUE.equals(cell.isHit()))
                .isShipNeighbor(Boolean.TRUE.equals(cell.isShipNeighbor()))
                .isKilledShipNeighborCell(Boolean.TRUE.equals(cell.isKilledShipNeighbor()))
                .ship(getShip(cell, showShips))
                .build();
    }

    private static ShipDto getShip(final BattlefieldCell cell, final boolean showShips) {
        final var ship = cell.getShip();
        if (showShips){
            return ShipConverter.convertShip(ship);
        }
        if (ship != null && cell.isHit()) {
            return ShipConverter.convertShip(ship);
        }
        return null;
    }
}
