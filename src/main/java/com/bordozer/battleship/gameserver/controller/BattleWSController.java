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

@Controller
@RequiredArgsConstructor
public class BattleWSController {

    private final GameService gameService;
    private final BattleService battleService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/player-move-in")
    public void playerMove(final PlayerMoveDto move) {
        battleService.move(move.getGameId(), move.getPlayerId(), PlayerMove.of(move.getLine(), move.getColumn()));
        sendPlayerNewGameState(move.getGameId(), move.getPlayerId());
        sendPlayerNewGameState(move.getGameId(), move.getPlayerId());
    }

    @MessageMapping("/game-event-in")
    public void onGameEvent(final GameEventDto gameEvent) {
        final var players = gameService.getGamePlayers(gameEvent.getGameId());
        sendPlayerNewGameState(gameEvent.getGameId(), players.getPlayer1Id());
        sendPlayerNewGameState(gameEvent.getGameId(), players.getPlayer12d());
    }

    private void sendPlayerNewGameState(final String gameId, final String playerId) {
        simpMessagingTemplate.convertAndSend(destination(gameId, playerId), battleService.getGameState(gameId, playerId));
    }

    private String destination(final String gameId, final String playerId) {
        return String.format("/game-state-changed/%s/%s", gameId, playerId);
    }
}
