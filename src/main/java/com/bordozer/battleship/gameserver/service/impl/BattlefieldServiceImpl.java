package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.BattlefieldCell;
import com.bordozer.battleship.gameserver.model.Game;
import com.bordozer.battleship.gameserver.model.LogItem;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.model.Ship;
import com.bordozer.battleship.gameserver.service.BattlefieldService;
import com.bordozer.battleship.gameserver.service.PlayerService;
import com.bordozer.battleship.gameserver.utils.CellUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattlefieldServiceImpl implements BattlefieldService {

    private final PlayerService playerService;

    @Override
    public void move(final Game game, final Battlefield battlefield, final String playerId, final PlayerMove move) {
        final var logs = new ArrayList<LogItem>();
        final var player = playerService.getById(playerId);

        final var cells = battlefield.getCells();
        final var cell = CellUtils.getCell(cells, move);
        cell.setHit(true);

        var damage = " - missed";
        final var ship = cell.getShip();
        if (ship != null) {
            ship.damage();
            final var isKilled = ship.isKilled();
            damage = isKilled ? " - KILLED" : " - DAMAGED";
            if (isKilled) {
                markNeighbourCells(cells);
                if (!hasAliveShips(cells)) {
                    game.setWinnerId(playerId);
                }
            }
        }

        logs.add(LogItem.builder().text(String.format("%s: %s%s", player.getName(), cell.humanize(), damage)).build());

        game.getBattle().addLogs(logs);
    }

    private boolean hasAliveShips(final List<List<BattlefieldCell>> cells) {
        final List<Ship> ships = CellUtils.collectShips(cells);
        return ships.stream().anyMatch(ship -> !ship.isKilled());
    }

    private void markNeighbourCells(final List<List<BattlefieldCell>> cells) {
        // TODO
    }
}
