package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.GameEventDto;
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
        sendGameState(move.getGameId());
    }

    @MessageMapping("/game-event-in")
    public void gameEvent(final GameEventDto gameEvent) {
        sendGameState(gameEvent.getGameId());
    }

    private void sendGameState(final String gameId) {
        simpMessagingTemplate.convertAndSend(destination(gameId), battleService.getBattle(gameId));
    }

    private String destination(final String gameId) {
        return String.format("/game-state-changed/%s", gameId);
    }
}
