package com.bordozer.battleship.multiplayer.service;

import com.bordozer.battleship.multiplayer.dto.EventType;
import com.bordozer.battleship.multiplayer.dto.GameNotificationDto;
import com.bordozer.battleship.multiplayer.model.LogItem;
import com.bordozer.battleship.multiplayer.model.PlayerMove;

import javax.annotation.CheckForNull;
import java.util.List;

public interface NotificationService {

    @CheckForNull
    GameNotificationDto gameEvent(String gameId, String eventProducerPlayerId, EventType eventType);

    @CheckForNull
    GameNotificationDto playerMove(String gameId, String playerWhoDidMoveId, PlayerMove playerMove, List<LogItem> moveLogs);
}
