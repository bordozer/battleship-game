package com.bordozer.battleship.gameserver.controller;

import com.bordozer.battleship.gameserver.dto.GameEventDto;
import com.bordozer.battleship.gameserver.dto.PlayerMoveDto;
import com.bordozer.battleship.gameserver.dto.battle.BattleDto;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.service.BattleService;
import com.bordozer.battleship.gameserver.service.GameEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BattleWSController {

    private final BattleService battleService;
    private final GameEventService gameEventService;

    @MessageMapping("/player-move-in")
    @SendTo("/game-state-changed")
    public BattleDto playerMove(final PlayerMoveDto move) {
        battleService.move(move.getGameId(), move.getPlayerId(), PlayerMove.of(move.getLine(), move.getColumn()));
        return battleService.getBattle(move.getGameId());
    }

    @MessageMapping("/game-event-in")
    @SendTo("/game-state-changed")
    public BattleDto gameEvent(final GameEventDto gameEvent) {
        //        gameEventService.process(gameEvent); // TODO: not sure I need this service at all
        return battleService.getBattle(gameEvent.getGameId());
    }
}
