package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.dto.EventType;
import com.bordozer.battleship.multiplayer.dto.GameNotificationDto;
import com.bordozer.battleship.multiplayer.model.LogItem;
import com.bordozer.battleship.multiplayer.model.PlayerMove;
import com.bordozer.battleship.multiplayer.service.GameService;
import com.bordozer.battleship.multiplayer.service.NotificationService;
import com.bordozer.battleship.multiplayer.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.stream.Collectors;

import static com.bordozer.battleship.multiplayer.dto.EventType.PLAYER_DID_MOVE;
import static com.google.common.collect.Lists.newArrayList;

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
        return GameNotificationDto.builder()
                .gameId(gameId)
                .playerId(notifiablePlayer.getId())
                .eventType(eventType)
                .messages(getMessages(eventType, eventProducerPlayerId))
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
        final var notifiableLogItems = getNotifiablePlayerMoveLogItems(moveLogs);
        if (CollectionUtils.isEmpty(notifiableLogItems)) {
            return null;
        }
        return GameNotificationDto.builder()
                .gameId(gameId)
                .playerId(notifiablePlayer.getId())
                .eventType(PLAYER_DID_MOVE)
                .messages(convertToNotificationMessage(notifiableLogItems))
                .build();
    }

    private List<String> getMessages(final EventType eventType, final String eventProducerPlayerId) {
        final var eventProducerPlayer = playerService.getById(eventProducerPlayerId);
        switch (eventType) {
            case PLAYER_JOINED_GAME:
                return newArrayList(String.format("%s joined the game", eventProducerPlayer.getName()));
            case PLAYER_CANCELLED_GAME:
                return newArrayList(String.format("%s cancelled the game", eventProducerPlayer.getName()));
            default:
                throw new IllegalStateException(String.format("Unsupported game event type: '%s'", eventType));
        }
    }

    private List<LogItem> getNotifiablePlayerMoveLogItems(final List<LogItem> moveLogs) {
        return moveLogs.stream().filter(LogItem::getNotifiable).collect(Collectors.toList());
    }

    private List<String> convertToNotificationMessage(final List<LogItem> moveLogs) {
        return moveLogs.stream()
                .map(LogItem::getText)
                .collect(Collectors.toList());
    }

    @CheckForNull
    private String getNotifiablePlayerId(final String gameId, final String eventProducerPlayerId) {
        final var gamePlayers = gameService.getGamePlayers(gameId);
        return eventProducerPlayerId.equals(gamePlayers.getPlayer1Id()) ? gamePlayers.getPlayer2Id() : gamePlayers.getPlayer1Id();
    }
}
