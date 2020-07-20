package com.bordozer.battleship.multiplayer.service.impl;

import com.bordozer.battleship.multiplayer.dto.battle.PlayerType;
import com.bordozer.battleship.multiplayer.model.Battlefield;
import com.bordozer.battleship.multiplayer.model.BattlefieldCell;
import com.bordozer.battleship.multiplayer.model.Game;
import com.bordozer.battleship.multiplayer.model.LogItem;
import com.bordozer.battleship.multiplayer.model.PlayerMove;
import com.bordozer.battleship.multiplayer.model.Ship;
import com.bordozer.battleship.multiplayer.service.BattlefieldService;
import com.bordozer.battleship.multiplayer.service.PlayerService;
import com.bordozer.battleship.multiplayer.utils.CellUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bordozer.battleship.multiplayer.model.GameState.FINISHED;
import static com.bordozer.battleship.multiplayer.utils.CellUtils.getShipNeighbourCells;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattlefieldServiceImpl implements BattlefieldService {

    private final PlayerService playerService;

    @Value("${application.battlefield.size}")
    private int battlefieldSize;

    @Override
    public List<LogItem> move(final Game game, final Battlefield battlefield, final String playerId, final PlayerMove move) {
        final var battle = game.getBattle();
        final var logs = new ArrayList<LogItem>();
        final var player = playerService.getById(playerId);

        final var cells = battlefield.getCells();
        final var cell = CellUtils.getCell(cells, move);

        if (cell.isHit()) {
            logs.add(LogItem.builder()
                    .text(String.format("%s: %s - cell has already been hit. Try another one", player.getName(), cell.humanize()))
                    .notifiable(Boolean.FALSE)
                    .build());
            battle.addLogs(logs);
            return logs;
        }
        if (cell.isKilledShipNeighbor()) {
            logs.add(LogItem.builder()
                    .text(String.format("%s: %s - cell is killed ship neighbor and cannot contains a ship. Try another cell", player.getName(), cell.humanize()))
                    .notifiable(Boolean.FALSE)
                    .build());
            battle.addLogs(logs);
            return logs;
        }

        cell.setHit(true);
        game.getBattlefieldFor(playerId).setLastShot(move);

        var shotResult = " - missed";
        final var ship = cell.getShip();
        if (ship != null) {
            ship.damage();
            final var isKilled = ship.isKilled();
            shotResult = isKilled ? " - KILLED" : " - DAMAGED";
            if (isKilled) {
                markNeighbourCells(ship.getShipId(), cells);
                if (!hasAliveShips(cells)) {
                    game.setWinnerId(playerId);
                    game.setState(FINISHED);
                }
            }
        }
        logs.add(LogItem.builder().text(String.format("%s: %s%s", player.getName(), cell.humanize(), shotResult)).build());

        if (StringUtils.isNotEmpty(game.getWinnerId())) {
            logs.add(LogItem.builder().text(String.format("%s wins!", player.getName())).build());
        }

        if (ship == null) {
            battle.setCurrentMove(game.getPlayer1Id().equals(playerId) ? PlayerType.PLAYER2 : PlayerType.PLAYER1);
        }

        battle.addLogs(logs);

        return logs;
    }

    private boolean hasAliveShips(final List<List<BattlefieldCell>> cells) {
        final List<Ship> ships = CellUtils.collectShips(cells);
        return ships.stream().anyMatch(ship -> !ship.isKilled());
    }

    private void markNeighbourCells(final String shipId, final List<List<BattlefieldCell>> cells) {
        getShipNeighbourCells(shipId, cells, battlefieldSize)
                .forEach(cell -> cell.setKilledShipNeighbor(true));
    }
}
