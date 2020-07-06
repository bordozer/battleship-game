package com.bordozer.battleship.multiplayer.converter;

import com.bordozer.battleship.multiplayer.dto.GameDto;
import com.bordozer.battleship.multiplayer.dto.GamePlayerDto;
import com.bordozer.battleship.multiplayer.dto.battle.GameStep;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.GameState;
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
                .gameStep(convertGameState(game.getState()))
                .build();
    }

    public static GameStep convertGameState(final GameState state) {
        switch (state) {
            case OPEN:
                return GameStep.WAITING_FOR_OPPONENT;
            case BATTLE:
                return GameStep.BATTLE;
            case FINISHED:
                return GameStep.FINISHED;
            case CANCELLED:
                return GameStep.CANCELLED;
            default:
                throw new IllegalArgumentException(String.format("Unsupported game state: '%s'", state));
        }
    }
}
