package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.GameStateRequestDto;
import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.service.BattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BattleWSController {

    private final BattleService battleService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/player-move-in")
    public void playerMove(final PlayerMoveDto move) {
        battleService.move(move.getGameId(), move.getPlayerId(), PlayerMove.of(move.getLine(), move.getColumn()));
        sendPlayerGameState(move.getGameId(), move.getPlayerId());
    }

    @MessageMapping("/game-event-in")
    public void gameEvent(final GameStateRequestDto gameStateRequest) {
        sendPlayerGameState(gameStateRequest.getGameId(), gameStateRequest.getPlayerId());
    }

    private void sendPlayerGameState(final String gameId, final String playerId) {
        simpMessagingTemplate.convertAndSend(destination(gameId, playerId), battleService.getGameState(gameId, playerId));
    }

    private String destination(final String gameId, final String playerId) {
        return String.format("/game-state-changed/%s/%s", gameId, playerId);
    }
}
