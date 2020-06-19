package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.BattleDto;
import com.bordozer.battleship.gameserver.dto.HelloMessage;
import com.bordozer.battleship.gameserver.dto.ImmutableBattleDto;
import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BattleController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public BattleDto greetings(final HelloMessage move) {
        return ImmutableBattleDto.builder()
                .gameId(move.getName())
                .build();
    }
}
