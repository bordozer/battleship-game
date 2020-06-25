package com.bordozer.battleship.gameserver.utils;

import com.bordozer.battleship.gameserver.model.Game;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.AccessControlException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    public static void assertAccess(final Game game, final String playerId) {
        if (!playerId.equals(game.getPlayer1Id()) && !playerId.equals(game.getPlayer2Id())) {
            throw new AccessControlException(String.format("Cannot delete %s", game.getGameId())); // TODO: map to AccessDenied http responce status
        }
    }
}
