package com.bordozer.battleship.multiplayer;

import com.bordozer.battleship.multiplayer.config.WSConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WSConfigurer.class)
public class BattleshipApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BattleshipApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BattleshipApplication.class);
    }
}
