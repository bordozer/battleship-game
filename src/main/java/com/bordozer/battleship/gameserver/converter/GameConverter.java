package com.bordozer.battleship.gameserver.converter;

import com.bordozer.battleship.gameserver.dto.GameDto;
import com.bordozer.battleship.gameserver.dto.GamePlayerDto;
import com.bordozer.battleship.gameserver.model.Game;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.annotation.CheckForNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameConverter {

    public static GameDto toDto(final Game game, final GamePlayerDto player1, @CheckForNull final GamePlayerDto player2) {
        return GameDto.builder()
                .gameId(game.getGameId())
                .player1(player1)
                .player2(player2)
                .build();
    }
}
