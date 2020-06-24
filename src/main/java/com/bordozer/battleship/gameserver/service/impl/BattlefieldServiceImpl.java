package com.bordozer.battleship.gameserver.service.impl;

import com.bordozer.battleship.gameserver.model.Battlefield;
import com.bordozer.battleship.gameserver.model.PlayerMove;
import com.bordozer.battleship.gameserver.service.BattlefieldService;
import com.bordozer.battleship.gameserver.utils.CellUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BattlefieldServiceImpl implements BattlefieldService {

    @Override
    public void move(final Battlefield battlefield, final PlayerMove move) {
        final var cell = CellUtils.getCell(battlefield.getCells(), move);
        cell.setHit(true);
        if (cell.getShip() != null) {
            // TODO: populate neighbour cells
        }
    }
}
