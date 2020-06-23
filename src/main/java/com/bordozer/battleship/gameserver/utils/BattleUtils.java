package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.model.Battle;
import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BattleUtils {

    private static final String[] X_AXE = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "K"};

    public static Battle initBattle() {
        return Battle.builder()
                .battlefield1(new Battlefield(initCells()))
                .battlefield2(new Battlefield(initCells()))
                .build();
    }

    private static List<List<BattlefieldCell>> initCells() {
        final List<List<BattlefieldCell>> columns = new ArrayList<>();
        for (int column = 0; column < 9; column++) {
            final var lines = new ArrayList<BattlefieldCell>();
            for (int line = 0; line < 9; line++) {
                final var xLabel = X_AXE[line];
                final var yLabel = String.valueOf(column + 1);
                lines.add(new BattlefieldCell(column, line, xLabel, yLabel));
            }
            columns.add(lines);
        }
        return columns;
    }
}
