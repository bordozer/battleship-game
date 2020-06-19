package com.bordozer.battleship.gameserver.config;

import com.bordozer.battleship.gameserver.handlers.GameHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public GameHandler gameHandler() {
        return new GameHandler();
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry
                .addHandler(gameHandler(), "/session")
                .setAllowedOrigins("*");
    }
}
