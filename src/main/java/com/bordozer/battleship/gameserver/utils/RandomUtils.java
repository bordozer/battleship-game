package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.dto.battle.PlayerType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

import static com.bordozer.battleship.gameserver.dto.battle.PlayerType.PLAYER1;
import static com.bordozer.battleship.gameserver.dto.battle.PlayerType.PLAYER2;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtils {

    public static PlayerType randomizeFirstMove() {
        return randomBoolean() ? PLAYER1 : PLAYER2;
    }

    public static boolean randomBoolean() {
        return new Random().nextBoolean();
    }
}
