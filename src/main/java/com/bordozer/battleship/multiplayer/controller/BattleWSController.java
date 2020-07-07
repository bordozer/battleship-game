package com.bordozer.battleship.multiplayer.controller;

import com.bordozer.battleship.multiplayer.dto.GameEventDto;
import com.bordozer.battleship.multiplayer.dto.GameNotificationDto;
import com.bordozer.battleship.multiplayer.dto.PlayerMoveDto;
import com.bordozer.battleship.multiplayer.model.PlayerMove;
import com.bordozer.battleship.multiplayer.service.BattleService;
import com.bordozer.battleship.multiplayer.service.GameService;
import com.bordozer.battleship.multiplayer.service.NotificationService;
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
    private final NotificationService notificationService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/game-event-in")
    public void onGameEvent(final GameEventDto gameEvent) {
        LOGGER.info("WS/game-event-in \"{}\"", LoggableJson.of(gameEvent));

        final var notification = notificationService.gameEvent(gameEvent.getGameId(), gameEvent.getPlayerId(), gameEvent.getEventType());
        sendGameStateToPlayers(gameEvent.getGameId(), gameEvent.getPlayerId());
        sendNotification(notification);
    }

    @MessageMapping("/player-move-in")
    public void playerMove(final PlayerMoveDto move) {
        LOGGER.info("WS/player-move-in \"{}\"", LoggableJson.of(move));

        final var playerMove = PlayerMove.of(move.getLine(), move.getColumn());
        final var logs = battleService.move(move.getGameId(), move.getPlayerId(), playerMove);
        final var notification = notificationService.playerMove(move.getGameId(), move.getPlayerId(), playerMove, logs);
        sendGameStateToPlayers(move.getGameId(), move.getPlayerId());
        sendNotification(notification);
    }

    private void sendGameStateToPlayers(final String gameId, final String eventSourcePlayerId) {
        final var players = gameService.getGamePlayers(gameId);
        final var player1Id = players.getPlayer1Id();
        final var player2Id = players.getPlayer2Id();

        sendGameStateToPlayer(gameId, player1Id);
        if (player2Id != null) {
            sendGameStateToPlayer(gameId, player2Id);
        }
    }

    private void sendGameStateToPlayer(final String gameId, final String playerId) {
        final var destination = String.format("/game-state-changed/%s/%s", gameId, playerId);
        simpMessagingTemplate.convertAndSend(destination, battleService.getGameState(gameId, playerId));
    }

    private void sendNotification(@CheckForNull final GameNotificationDto notification) {
        if (notification == null) {
            return;
        }
        final var destination = String.format("/game-notification/%s/%s", notification.getGameId(), notification.getPlayerId());
        simpMessagingTemplate.convertAndSend(destination, notification);
    }
}
