package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.model.Battle;
import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.LogItem;
import com.bordozer.battleship.gameserver.model.Ship;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.bordozer.battleship.gameserver.dto.battle.CurrentMove.PLAYER1;
import static com.google.common.collect.Lists.newArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BattleUtils {

    private static final String[] X_AXE = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "K"};

    public static Battle initBattle(final ArrayList<ArrayList<CellDto>> cells) {
        return Battle.builder()
                .battlefield1(new Battlefield(convertCells(cells)))
                .battlefield2(new Battlefield(Collections.emptyList()))
                .currentMove(null)
                .logs(newArrayList(LogItem.builder().text("Game has been created").build()))
                .build();
    }

    /* TODO: move to converter */
    public static List<List<BattlefieldCell>> convertCells(final ArrayList<ArrayList<CellDto>> cells) {
        final List<List<BattlefieldCell>> columns = new ArrayList<>();
        for (int column = 0; column < 9; column++) {
            final var lines = new ArrayList<BattlefieldCell>();
            final var player1Lines = cells.get(column);
            for (int line = 0; line < 9; line++) {
                final var xLabel = X_AXE[line];
                final var yLabel = String.valueOf(column + 1);

                final var player1Cell = player1Lines.get(line);

                final var cell = new BattlefieldCell(column, line, xLabel, yLabel);
                final var ship = player1Cell.getShip();
                if (ship != null) {
                    cell.setShip(Ship.builder().shipId(ship.getId()).name(ship.getName()).size(ship.getSize()).damage(ship.getDamage()).build());
                }
                lines.add(cell);
            }
            columns.add(lines);
        }
        return columns;
    }
}
