package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.converter.CellConverter;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.model.Battle;
import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.LogItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BattleUtils {

    public static Battle initBattle(final ArrayList<ArrayList<CellDto>> cells) {
        return Battle.builder()
                .battlefield1(new Battlefield(CellConverter.convertCells(cells)))
                .battlefield2(new Battlefield(emptyCells()))
                .currentMove(null)
                .logs(newArrayList(LogItem.builder().text("Game has been created").build()))
                .build();
    }

    private static List<List<BattlefieldCell>> emptyCells() {
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
}
