/* jshint esversion: 6 */
import React from 'react';
import {withRouter} from 'react-router-dom';

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faShip, faTrashAlt, faUserCheck, faUserPlus} from '@fortawesome/free-solid-svg-icons';

import {initBattleFieldCells} from 'src/utils/battle-field-utils';
import {generateShips} from 'src/utils/ships-generator';
import BattleFieldRenderer from 'components/battle-field-renderer';
import ShipsStateRenderer from 'components/ships-state';
import LogsRenderer from 'components/logs-renderer';
import {getUserIdFromCookie} from 'src/utils/cookies-utils';
import {cancelGame, createGame, getGameState, joinGame} from 'src/utils/game-flow';
import {showNotification} from 'src/utils/notification';
import Spinner from 'src/utils/spinner';

import Swal from 'sweetalert2';

const qs = require('query-string');

const STEP_GAME_INIT = 'GAME_INIT';
const STEP_WAITING_FOR_OPPONENT = 'WAITING_FOR_OPPONENT';
const STEP_BATTLE = 'BATTLE';
const STEP_FINISHED = 'FINISHED';
const STEP_CANCELLED = 'CANCELLED';

const NO_EVENT = 'NO_EVENT';

let stompClient = null;

function connect(gameId, playerId, eventType, setStateCallback, notificationCallback) {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {

        if (eventType !== NO_EVENT) {
            notifyAboutGameEvent(gameId, playerId, eventType);
        }

        const gameStateSubscription = '/game-state-changed/' + gameId + '/' + playerId;
        stompClient.subscribe(gameStateSubscription, function (battle) {
            setStateCallback(JSON.parse(battle.body));
        });

        const notificationSubscription = '/game-notification/' + gameId + '/' + playerId;
        stompClient.subscribe(notificationSubscription, function (notification) {
            notificationCallback(gameId, JSON.parse(notification.body));
        });
    });
}

function notifyAboutGameEvent(gameId, playerId, eventType) {
    stompClient.send('/game-event-in', {}, JSON.stringify({
            'gameId': gameId,
            'playerId': playerId,
            'eventType': eventType
        }
    ));
}

function sendMove(gameId, playerId, line, column) {
    stompClient.send('/player-move-in', {}, JSON.stringify({
            'gameId': gameId,
            'playerId': playerId,
            'line': line,
            'column': column
        }
    ));
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

class Layout extends React.Component {

    componentDidMount() {
        const gameId = qs.parse(location.search).gameId;

        fetch('/api/whoami')
            .then(response => response.json())
            .then(data => {
                if (gameId) {
                    this.setGameStateFromServer(gameId, data.player);
                    return;
                }
                this.setState(this.getInitialState(data.player));
            });
    };

    onGenerateShipsClick = () => {
        const cells = initBattleFieldCells(10);
        const ships = generateShips(cells);

        this.setState((state) => {
            return {
                player: {
                    playerId: state.player.playerId,
                    playerName: state.player.playerName,
                    cells: cells,
                    ships: ships,
                    lastShot: null,
                    damagedShipCells: []
                },
            };
        });
    };

    onCreateGameClick = () => {
        const callback = function (gameId) {
            this.setState({
                gameplay: {
                    gameId: gameId,
                    creatorPlayerId: this.state.gameplay.creatorPlayerId,
                    step: STEP_WAITING_FOR_OPPONENT,
                    currentMove: null,
                    winner: null
                }
            });
            this.connect(NO_EVENT);
        }.bind(this);
        createGame(this.state.player.cells, callback);
    };

    onJoinGameClick = () => {
        const callback = function (result) {
            this.connect('PLAYER_JOINED_GAME');
        }.bind(this);
        joinGame(this.state.gameplay.gameId, this.state.player.cells, callback);
    };

    onCancelGameClick = () => {
        const gameId = this.state.gameplay.gameId;
        const playerId = this.state.player.playerId;
        const callback = function () {
            notifyAboutGameEvent(gameId, playerId, 'PLAYER_CANCELLED_GAME');
            disconnect();
            this.props.history.push('/');
        }.bind(this);
        cancelGame(gameId, callback);
    };

    connect = (eventType) => {
        const gameId = this.state.gameplay.gameId;
        const playerId = this.state.player.playerId;
        connect(gameId, playerId, eventType, this.updateGameState.bind(this), this.notification.bind(this));
    };

    getInitialState = (player) => {
        // console.log('====== player', player);
        const cells = initBattleFieldCells(10);
        const ships = generateShips(cells);

        return {
            player: {
                playerId: player.id,
                playerName: player.name,
                cells: cells,
                ships: ships,
                lastShot: null,
                damagedShipCells: []
            },
            enemy: {
                playerId: null,
                playerName: '???',
                cells: initBattleFieldCells(10),
                ships: [],
                lastShot: null,
                damagedShipCells: []
            },
            // TODO: delete 'config' section - is not used any more
            config: {
                showShotHints: false,
                difficulty: 3,
            },
            gameplay: {
                gameId: '',
                creatorPlayerId: player.id,
                step: STEP_GAME_INIT,
                currentMove: null,
                winner: null
            },
            logs: []
        };
    };

    setGameStateFromServer = (gameId, player) => {
        const callback = function (serverState) {
            if (serverState.player.ships.length === 0) {
                const cells = initBattleFieldCells(10);
                const ships = generateShips(cells);
                serverState.player.playerId = player ? player.id : serverState.player.playerId;
                serverState.player.playerName = player ? player.name : serverState.player.playerName;
                serverState.player.cells = cells;
                serverState.player.ships = ships;
            }
            this.setState(serverState);
            if (serverState.player.playerId === getUserIdFromCookie()) {
                this.connect(NO_EVENT);
            }
        }.bind(this);

        getGameState(gameId, callback);
    };

    playerCellSetup = (cell) => {
    };

    playerShot = (cell) => {
        // console.log("Player's shot", cell);
        sendMove(this.state.gameplay.gameId, this.state.player.playerId, cell.y, cell.x);
    };

    updateGameState = (newState) => {
        this.setState(newState, () => this.stateUpdateCallback());
    };

    notification = (gameId, notification) => {
        showNotification(gameId, notification);
    };

    stateUpdateCallback = () => {
        const gameState = this.state.gameplay.step;
        const winner = this.state.gameplay.winner;
        if (gameState === STEP_CANCELLED) {
            Swal.fire(
                'You have won',
                'Your opponent has cancelled the game (gave up)',
                'success'
            );
            disconnect();
        }
        if (winner === 'player') {
            Swal.fire(
                'You have won',
                'Congratulations, ' + this.state.player.playerName,
                'success'
            );
            disconnect();
        }
        if (winner === 'enemy') {
            Swal.fire(
                'You lose',
                'Congratulations, ' + this.state.enemy.playerName,
                'error'
            );
            disconnect();
        }
    };

    render() {
        if (!this.state) {
            return (
                <Spinner/>
            );
        }
        console.log('this.state', JSON.stringify(this.state));

        const gameplay = this.state.gameplay;
        const currentMove = gameplay.currentMove;
        const gameId = gameplay.gameId;
        const player = this.state.player;
        const enemy = this.state.enemy;
        const gameStep = gameplay.step;

        const playerBattleFieldOpts = {
            isPlayer: true,
            stage: gameStep,
            lastShot: enemy.lastShot,
            currentMove: currentMove,
            highlightBattleArea: gameStep === STEP_BATTLE && currentMove === 'enemy',
            recommendedShots: {
                shots: [],
                strategy: null
            }
        };

        const enemyBattleFieldOpts = {
            isPlayer: false,
            stage: gameStep,
            lastShot: player.lastShot,
            recommendedShots: {
                shots: [],
                strategy: null
            },
            highlightBattleArea: gameStep === STEP_BATTLE && currentMove === 'player',
            currentMove: currentMove,
        };

        const isGameCreator = player.playerId === gameplay.creatorPlayerId;
        const btnShips = (gameStep === STEP_GAME_INIT)
            || (gameStep === STEP_WAITING_FOR_OPPONENT && !isGameCreator);
        const btnCreateGame = gameId === '' && gameStep === STEP_GAME_INIT;
        const btnJoinGame = gameId !== '' && gameStep === STEP_WAITING_FOR_OPPONENT && !isGameCreator;
        const btnCancelGame = (gameStep === STEP_WAITING_FOR_OPPONENT) || (gameStep === STEP_BATTLE);

        return (
            <div>
                <div className="row mt-10">
                    <div className="col-sm-1">
                        <ShipsStateRenderer
                            playerName={player.playerName}
                            ships={player.ships}
                            isPlayer={true}
                            winner={gameplay.winner}
                        />
                    </div>
                    <div
                        className={'col-sm-5' + (playerBattleFieldOpts.highlightBattleArea ? '' : ' disabledArea')}
                        disabled={playerBattleFieldOpts.highlightBattleArea}>
                        <BattleFieldRenderer
                            cells={player.cells}
                            options={playerBattleFieldOpts}
                            onCellClick={this.playerCellSetup}
                        />
                    </div>
                    <div
                        className={'col-sm-5' + (enemyBattleFieldOpts.highlightBattleArea ? '' : ' disabledArea')}
                        disabled={enemyBattleFieldOpts.highlightBattleArea}>
                        <BattleFieldRenderer
                            cells={enemy.cells}
                            options={enemyBattleFieldOpts}
                            onCellClick={this.playerShot}
                        />
                    </div>
                    <div className="col-sm-1">
                        <ShipsStateRenderer
                            playerName={enemy.playerName}
                            ships={enemy.ships}
                            isPlayer={false}
                            winner={gameplay.winner}
                        />
                    </div>
                </div>

                <div className="row mt-10">
                    <div className="col-sm-4"/>
                    <div className="col-sm-4 text-center btn-lg">
                        <button
                            className="bg-light button-rounded"
                            onClick={this.onGenerateShipsClick}
                            disabled={!btnShips}
                            title='Generate random ships'>
                            <FontAwesomeIcon icon={faShip}/>
                        </button>
                        <button
                            className="bg-light button-rounded ml-10"
                            onClick={this.onCreateGameClick}
                            disabled={!btnCreateGame}
                            title='Create game'>
                            <FontAwesomeIcon icon={faUserCheck}/>
                        </button>
                        <button
                            className="bg-light button-rounded ml-10"
                            onClick={this.onJoinGameClick}
                            disabled={!btnJoinGame}
                            title='Join game'>
                            <FontAwesomeIcon icon={faUserPlus}/>
                        </button>
                        <button
                            className="bg-light button-rounded ml-10"
                            onClick={this.onCancelGameClick}
                            disabled={!btnCancelGame}
                            title='Cancel game'>
                            <FontAwesomeIcon icon={faTrashAlt}/>
                        </button>
                    </div>
                    <div className="col-sm-4"/>
                </div>

                <div className="row mt-10">
                    <div className="col-sm-12">
                        <LogsRenderer logs={this.state.logs}/>
                    </div>
                </div>

            </div>
        );
    }
}

export default withRouter(Layout);
