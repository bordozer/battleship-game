package com.bordozer.battleship.multiplayer.controller;

import com.bordozer.battleship.multiplayer.dto.GameEventDto;
import com.bordozer.battleship.multiplayer.dto.PlayerMoveDto;
import com.bordozer.battleship.multiplayer.model.PlayerMove;
import com.bordozer.battleship.multiplayer.service.BattleService;
import com.bordozer.battleship.multiplayer.service.GameService;
import com.bordozer.commons.utils.LoggableJson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.CheckForNull;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BattleWSController {

    private final GameService gameService;
    private final BattleService battleService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/player-move-in")
    public void playerMove(final PlayerMoveDto move) {
        LOGGER.info("WS/player-move-in \"{}\"", LoggableJson.of(move));
        battleService.move(move.getGameId(), move.getPlayerId(), PlayerMove.of(move.getLine(), move.getColumn()));
        sendNewGameStateToPlayers(move.getGameId());
    }

    @MessageMapping("/game-event-in")
    public void onGameEvent(final GameEventDto gameEvent) {
        LOGGER.info("WS/game-event-in \"{}\"", LoggableJson.of(gameEvent));
        sendNewGameStateToPlayers(gameEvent.getGameId());
    }

    private void sendNewGameStateToPlayers(final String gameId) {
        final var players = gameService.getGamePlayers(gameId);
        sendNewGameStateToPlayer(gameId, players.getPlayer1Id());
        sendNewGameStateToPlayer(gameId, players.getPlayer12d());
    }

    private void sendNewGameStateToPlayer(final String gameId, @CheckForNull final String playerId) {
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
