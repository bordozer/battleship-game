package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.converter.CellConverter;
import com.bordozer.battleship.gameserver.dto.battle.CellDto;
import com.bordozer.battleship.gameserver.model.Battle;
import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.LogItem;
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
