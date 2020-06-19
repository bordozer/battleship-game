package com.bordozer.battleship.gameserver;

import com.bordozer.battleship.gameserver.config.WebSocketConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableWebSocketMessageBroker
@Import(WebSocketConfig.class)
public class BattleshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipApplication.class, args);
    }
}
