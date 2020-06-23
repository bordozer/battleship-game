package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.service.BattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BattleWSController {

    private final BattleService battleService;

    @MessageMapping("/inbound")
    @SendTo("/outbound")
    public BattleDto player1Move(final PlayerMoveDto move) {
        return constructResponse(move);
    }

    private BattleDto constructResponse(final PlayerMoveDto move) {
        return battleService.getBattle(move.getGameId());
    }
}
