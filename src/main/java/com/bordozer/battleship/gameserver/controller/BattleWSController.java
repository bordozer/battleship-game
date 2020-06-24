package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.model.GameEventType;
import com.bordozer.battleship.gameserver.service.BattleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
    public BattleDto gameEvent(final GameEventDto gameEvent) {
        // TODO: process the event
        return battleService.getBattle(gameEvent.getGameId());
    }

    @Getter
    @Setter
    @ToString
    private static class GameEventDto {
        private String gameId;
        private GameEventType event;
    }
}
