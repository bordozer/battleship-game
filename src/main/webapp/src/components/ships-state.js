/* jshint esversion: 6 */
import React from 'react';

import {getBiggestShip} from 'src/utils/ships-utils';

function _renderShip(ship, size, isPlayer) {
    const result = [];
    for (let i = 1; i <= size; i++) {
        const isShip = i <= ship.size;
        const isKilled = ship.damage === ship.size;
        result.push(
            <div
                key={'ship-stat-line-cell-' + ship.id + '-' + i + '-' + (isPlayer ? '-player' : '-enemy')}
                className={'stat-cell' + (isShip ? ' stat-cell-ship' : '') + (isShip && isKilled ? ' stat-cell-ship-killed' : '') + (isPlayer ? ' float-right' : '')}
                title={ship.name}
            >

            </div>
        );
    }
    return result;
}

const ShipStateRenderer = ({playerName, ships, isPlayer, winner}) => {
    if (!ships || ships.length === 0) {
        return "";
    }
    const biggestShip = getBiggestShip(ships);
    const result = [];
    ships.forEach(ship => {
        result.push(
            <div key={'ship-stat-line-' + ship.id} className='row mt-10'>
                <div key={'ship-stat-col-' + ship.id} className='col-sm-12'>
                    {_renderShip(ship, biggestShip.size, isPlayer)}
                </div>
            </div>
        )
    });

    const isLooser = (winner && ((isPlayer && winner === 'enemy') || (!isPlayer && winner === 'player')));

    return (
        <div>
            <div className="row mt-10">
                <div className={"col-sm-12 text-center" + (isLooser ? ' looser text-warning' : '')}>
                    {playerName}
                </div>
            </div>

            {result}
        </div>
    )
}

export default ShipStateRenderer;
