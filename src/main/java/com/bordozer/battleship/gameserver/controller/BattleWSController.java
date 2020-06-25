package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.GameEventDto;
import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.service.BattleService;
import com.bordozer.battleship.gameserver.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.CheckForNull;

@Controller
@RequiredArgsConstructor
public class BattleWSController {

    private final GameService gameService;
    private final BattleService battleService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/player-move-in")
    public void playerMove(final PlayerMoveDto move) {
        battleService.move(move.getGameId(), move.getPlayerId(), PlayerMove.of(move.getLine(), move.getColumn()));
        sendNewGameStateToPlayers(move.getGameId());
    }

    @MessageMapping("/game-event-in")
    public void onGameEvent(final GameEventDto gameEvent) {
        sendNewGameStateToPlayers(gameEvent.getGameId());
    }

    private void sendNewGameStateToPlayers(final String gameId) {
        final var players = gameService.getGamePlayers(gameId);
        sendPlayerNewGameState(gameId, players.getPlayer1Id());
        sendPlayerNewGameState(gameId, players.getPlayer12d());
    }

    private void sendPlayerNewGameState(final String gameId, @CheckForNull final String playerId) {
        if (playerId == null) {
            return;
        }
        final var destination = destination(gameId, playerId);
        simpMessagingTemplate.convertAndSend(destination, battleService.getGameState(gameId, playerId));
    }

    private String destination(final String gameId, final String playerId) {
        return String.format("/game-state-changed/%s/%s", gameId, playerId);
    }
}
