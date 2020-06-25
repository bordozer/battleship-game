package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.dto.battle.PlayerType;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bordozer.battleship.gameserver.model.GameState.FINISHED;
import static com.bordozer.battleship.gameserver.utils.CellUtils.getShipNeighbourCells;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattlefieldServiceImpl implements BattlefieldService {

    private final PlayerService playerService;

    @Override
    public void move(final Game game, final Battlefield battlefield, final String playerId, final PlayerMove move) {
        final var battle = game.getBattle();
        final var logs = new ArrayList<LogItem>();
        final var player = playerService.getById(playerId);

        final var cells = battlefield.getCells();
        final var cell = CellUtils.getCell(cells, move);

        if (cell.isHit()) {
            logs.add(LogItem.builder().text(String.format("%s: %s - cell has already been hit. Try another one", player.getName(), cell.humanize())).build());
            battle.addLogs(logs);
            return;
        }
        if (cell.isKilledShipNeighbor()) {
            logs.add(LogItem.builder().text(String.format("%s: %s - cell is killed ship neighbor and cannot contains a ship. Try another cell", player.getName(), cell.humanize())).build());
            battle.addLogs(logs);
            return;
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
    }

    private boolean hasAliveShips(final List<List<BattlefieldCell>> cells) {
        final List<Ship> ships = CellUtils.collectShips(cells);
        return ships.stream().anyMatch(ship -> !ship.isKilled());
    }

    private void markNeighbourCells(final String shipId, final List<List<BattlefieldCell>> cells) {
        getShipNeighbourCells(shipId, cells)
                .forEach(cell -> cell.setKilledShipNeighbor(true));
    }
}
