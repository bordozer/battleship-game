package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.dto.EventType;
import com.bordozer.battleship.multiplayer.dto.GameNotificationDto;
import com.bordozer.battleship.multiplayer.dto.GamePlayerDto;
import com.bordozer.battleship.multiplayer.dto.notification.GameEventNotificationDto;
import com.bordozer.battleship.multiplayer.dto.notification.Notifiable;
import com.bordozer.battleship.multiplayer.dto.notification.PlayerMoveNotificationDto;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.GamePlayers;
import com.bordozer.battleship.multiplayer.model.LogItem;
import com.bordozer.battleship.multiplayer.model.PlayerMove;
import com.bordozer.battleship.multiplayer.service.GameService;
import com.bordozer.battleship.multiplayer.service.NotificationService;
import com.bordozer.battleship.multiplayer.service.PlayerService;
import com.bordozer.battleship.multiplayer.utils.CellUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.CheckForNull;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final PlayerService playerService;
    private final GameService gameService;

    @Override
    @CheckForNull
    public GameNotificationDto gameEvent(final String gameId, final String eventProducerPlayerId, final EventType eventType) {
        @CheckForNull final var notifiablePlayerId = getNotifiablePlayerId(gameId, eventProducerPlayerId);
        if (notifiablePlayerId == null) {
            return null;
        }
        final var notifiablePlayer = playerService.getById(notifiablePlayerId);

        final var notification = GameEventNotificationDto.builder()
                .build();

        return GameNotificationDto.builder()
                .gameId(gameId)
                .playerId(notifiablePlayer.getId())
                .playerName(notifiablePlayer.getName())
                .eventType(eventType)
                .notification(notification)
                .build();
    }

    @Override
    @CheckForNull
    public GameNotificationDto playerMove(final String gameId, final String playerWhoDidMoveId, final PlayerMove playerMove, final List<LogItem> moveLogs) {
        @CheckForNull final var notifiablePlayerId = getNotifiablePlayerId(gameId, playerWhoDidMoveId);
        if (notifiablePlayerId == null) {
            return null;
        }
        final var notifiablePlayer = playerService.getById(notifiablePlayerId);

        final var notification = PlayerMoveNotificationDto.builder()
                .moveLogs(moveLogs)
                .build();

        return GameNotificationDto.builder()
                .gameId(gameId)
                .playerId(notifiablePlayer.getId())
                .playerName(notifiablePlayer.getName())
                .eventType(EventType.PLAYER_DID_MOVE)
                .notification(notification)
                .build();
    }

    @CheckForNull
    private String getNotifiablePlayerId(final String gameId, final String eventProducerPlayerId) {
        final var gamePlayers = gameService.getGamePlayers(gameId);
        return eventProducerPlayerId.equals(gamePlayers.getPlayer1Id()) ? gamePlayers.getPlayer2Id() : gamePlayers.getPlayer1Id();
    }
}
