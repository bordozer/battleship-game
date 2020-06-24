package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.model.GameEventType;
import com.bordozer.battleship.gameserver.service.BattleService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BattleWSController {

    private final BattleService battleService;

    @MessageMapping("/player-move-in")
    @SendTo("/game-state-changed")
    public BattleDto playerMove(final PlayerMoveDto move) {
        return battleService.getBattle(move.getGameId());
    }

    @MessageMapping("/game-event-in")
    @SendTo("/game-state-changed")
    public BattleDto gameEvent(final String gameId, final GameEventType event) {
        // TODO: process the event
        return battleService.getBattle(gameId);
    }
}
