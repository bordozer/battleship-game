package com.bordozer.battleship.multiplayer.utils;

import com.bordozer.battleship.multiplayer.converter.CellConverter;
import com.bordozer.battleship.multiplayer.dto.battle.CellDto;
import com.bordozer.battleship.multiplayer.model.Battle;
import com.bordozer.battleship.multiplayer.model.Battlefield;
import com.bordozer.battleship.multiplayer.model.LogItem;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BattleUtils {

    public static final int BATTLEFIELD_SIZE = 10;

    public static Battle initBattle(final ArrayList<ArrayList<CellDto>> cells) {
        return Battle.builder()
                .battlefield1(new Battlefield(CellConverter.convertCells(cells)))
                .battlefield2(new Battlefield(CellUtils.initCells()))
                .currentMove(null)
                .logs(newArrayList(LogItem.builder().text("Game has been created").build()))
                .build();
    }
}
