package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CellUtils {

    public static BattlefieldCell getCell(final List<List<BattlefieldCell>> cells, final PlayerMove move) {
        return cells.stream()
                .flatMap(Collection::stream)
                .filter(cell -> cell.getLine() == move.getLine() && cell.getColumn() == move.getColumn())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Player's move cannot be found on battlefield: %s", move)));
    }
}
