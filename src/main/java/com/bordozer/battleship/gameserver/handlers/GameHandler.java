package com.bordozer.battleship.gameserver.handlers;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GameHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }
}
