package com.bordozer.battleship.gameserver;

import com.bordozer.battleship.gameserver.config.WebSocketMBConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

//@EnableWebMvc
@SpringBootApplication
@Import(WebSocketMBConfigurer.class)
public class BattleshipApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BattleshipApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BattleshipApplication.class, args);
    }
}
