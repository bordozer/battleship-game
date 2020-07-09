/* jshint esversion: 6 */
import React from 'react';
import { withRouter } from "react-router-dom";
import $ from 'jquery';

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faShip, faTrashAlt, faUserCheck, faUserPlus} from '@fortawesome/free-solid-svg-icons';

import {initBattleFieldCells} from 'src/utils/battle-field-utils';
import {generateShips} from 'src/utils/ships-generator';
import BattleFieldRenderer from 'components/battle-field-renderer';
import ShipsStateRenderer from 'components/ships-state';
import LogsRenderer from 'components/logs-renderer';
import {getUserIdFromCookie} from 'src/utils/cookies-utils';
import {createGame, joinGame, cancelGame} from 'src/utils/game-flow';
import {showNotification} from 'src/utils/notification';

import Swal from 'sweetalert2';

const qs = require('query-string');

const STEP_GAME_INIT = 'GAME_INIT';
const STEP_WAITING_FOR_OPPONENT = 'WAITING_FOR_OPPONENT';
const STEP_BATTLE = 'BATTLE';

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
            notificationCallback(JSON.parse(notification.body));
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
    // console.log("Disconnected");
}

class Layout extends React.Component {

    constructor(props) {
        super(props);
        this.state = this.getInitialState(null);
    }

    componentDidMount() {
        const gameId = qs.parse(location.search).gameId;
        fetch('/api/whoami')
            .then(response => response.json())
            .then(data => {
                this.setState({
                    player: {
                        playerId: data.player.id,
                        playerName: data.player.name,
                        cells: this.state.player.cells,
                        ships: this.state.player.ships,
                        lastShot: null,
                        damagedShipCells: []
                    },
                    gameplay: {
                        gameId: gameId,
                        step: STEP_GAME_INIT,
                        currentMove: null,
                        winner: null
                    }
                });
                if (gameId !== '' && data.player.id === getUserIdFromCookie()) {
                    this.connect(NO_EVENT);
                }
                // console.log("PlayerId: ", data.player.id, 'Player name: ', data.player.name);
            });
    }

    onGenerateShipsClick = () => {
        this.setState((state) => {
            return this.getInitialState(state);
        });
    };

    onCreateGameClick = () => {
        const callback = function (gameId) {
            this.setState({
                gameplay: {
                    gameId: gameId,
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
        const beforeDeleteCallback = function () {
            notifyAboutGameEvent(gameId, playerId, 'PLAYER_CANCELLED_GAME');
        }.bind(this);
        const afterDeleteCallback = function () {
            disconnect();
            this.props.history.push("/");
        }.bind(this);
        cancelGame(gameId, beforeDeleteCallback, afterDeleteCallback);
    };

    connect = (eventType) => {
        const gameId = this.state.gameplay.gameId;
        const playerId = this.state.player.playerId;
        connect(gameId, playerId, eventType, this.updateGameState.bind(self), this.notification.bind(self));
    };

    getInitialState = (state) => {
        const playerData = this.randomizeBattleFieldWithShips();
        return {
            player: {
                playerId: state ? state.player.playerId : null,
                playerName: state ? state.player.playerId : 'Player',
                cells: playerData.cells,
                ships: playerData.ships,
                lastShot: null,
                damagedShipCells: []
            },
            enemy: {
                playerId: null,
                playerName: 'unknown yet',
                cells: initBattleFieldCells(10),
                ships: [],
                lastShot: null,
                damagedShipCells: []
            },
            config: {
                showShotHints: state ? state.config.showShotHints : true,
                difficulty: state ? state.config.difficulty : 3, /* 1 - easy, 2 - medium, 3 - hard */
            },
            gameplay: {
                gameId: state ? state.gameplay.gameId : '',
                step: STEP_GAME_INIT, /* TODO: for player2 is WAITING_FOR_OPPONENT */
                currentMove: null,
                winner: null
            },
            logs: []
        };
    };

    randomizeBattleFieldWithShips = () => {
        const cells = initBattleFieldCells(10);
        return generateShips(cells);
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

    notification = (notification) => {
        showNotification(notification);
    };

    stateUpdateCallback = () => {
        const winner = this.state.gameplay.winner;
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
        // console.log('this.state', JSON.stringify(this.state));

        const step = this.state.gameplay.step;
        const currentMove = this.state.gameplay.currentMove;
        const difficulty = this.state.config.difficulty;

        const playerBattleFieldOpts = {
            isPlayer: true,
            stage: step,
            lastShot: this.state.enemy.lastShot,
            currentMove: currentMove,
            highlightBattleArea: step === STEP_BATTLE && currentMove === 'enemy',
            recommendedShots: {
                shots: [],
                strategy: null
            }
        };

        const enemyBattleFieldOpts = {
            isPlayer: false,
            stage: step,
            lastShot: this.state.player.lastShot,
            recommendedShots: {
                shots: [],
                strategy: null
            },
            highlightBattleArea: step === STEP_BATTLE && currentMove === 'player',
            currentMove: currentMove,
        };

        const btnShips = this.state.gameplay.step === STEP_GAME_INIT;
        const btnCreateGame = this.state.gameplay.gameId === '' && this.state.gameplay.step === STEP_GAME_INIT;
        const btnJoinGame = this.state.gameplay.gameId !== '' && this.state.gameplay.step === STEP_GAME_INIT;
        const btnCancelGame = this.state.gameplay.step !== STEP_GAME_INIT;

        return (
            <div>
                <div className="row mt-10">
                    <div className="col-sm-1">
                        <ShipsStateRenderer
                            playerName={this.state.player.playerName}
                            ships={this.state.player.ships}
                            isPlayer={true}
                            winner={this.state.gameplay.winner}
                        />
                    </div>
                    <div
                        className={'col-sm-5' + (playerBattleFieldOpts.highlightBattleArea ? '' : ' disabledArea')}
                        disabled={playerBattleFieldOpts.highlightBattleArea}>
                        <BattleFieldRenderer
                            cells={this.state.player.cells}
                            options={playerBattleFieldOpts}
                            onCellClick={this.playerCellSetup}
                        />
                    </div>
                    <div
                        className={'col-sm-5' + (enemyBattleFieldOpts.highlightBattleArea ? '' : ' disabledArea')}
                        disabled={enemyBattleFieldOpts.highlightBattleArea}>
                        <BattleFieldRenderer
                            cells={this.state.enemy.cells}
                            options={enemyBattleFieldOpts}
                            onCellClick={this.playerShot}
                        />
                    </div>
                    <div className="col-sm-1">
                        <ShipsStateRenderer
                            playerName={this.state.enemy.playerName}
                            ships={this.state.enemy.ships}
                            isPlayer={false}
                            winner={this.state.gameplay.winner}
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
