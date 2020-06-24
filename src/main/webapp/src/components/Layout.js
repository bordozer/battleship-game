import React from "react"
import $ from "jquery"

import {initBattleFieldCells} from 'src/utils/battle-field-utils';
import {generateShips} from 'src/utils/ships-generator';
import BattleFieldRenderer from 'components/battle-field-renderer';
import GameConfigRenderer from 'components/game-config-renderer';
import ShipsStateRenderer from 'components/ships-state';
import LogsRenderer from 'components/logs-renderer';

const STEP_GAME_INIT = 'GAME_INIT';
const STEP_WAITING_FOR_OPPONENT = 'WAITING_FOR_OPPONENT';
const STEP_BATTLE = 'BATTLE';
const STEP_FINAL = 'FINAL';

let playerId = null;
let stompClient = null;

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/outbound', function (battle) {
            console.log("battle", battle.body);
            showGreeting(JSON.parse(battle.body));
        });
    });
}

function sendMove() {
    stompClient.send("/inbound", {}, JSON.stringify({
            'gameId': '8327de36-5a26-4034-a032-e7bc6b221084',
            'playerId': getPlayerId(),
            'line': 'E',
            'column': '4'
        }
    ));
}

function getPlayerId() {
    return playerId;
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.playerMove.playerId + ': ' + message.playerMove.line + message.playerMove.column + "</td></tr>");
}

/*function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}*/

export default class Layout extends React.Component {

    constructor(props) {
        super(props);
        this.state = this.getInitialState(null);
    }

    componentDidMount() {
        fetch('/whoami')
            .then(response => response.json())
            .then(data => {
                playerId = data.player.id // TODO: duplicates the state
                this.setState({
                    player: {
                        playerId: data.player.id,
                        playerName: data.player.name,
                        cells: this.state.player.cells,
                        ships: this.state.player.ships,
                        lastShot: null,
                        damagedShipCells: [],
                        points: this.state.player.points
                    }
                });
                console.log("PlayerId: ", data.player.id, 'Player name: ', data.player.name);
            });
    }

    onGenerateShipsClick = () => {
        this.setState((state) => {
            return this.getInitialState(state);
        });
    }

    onCreateGameClick = () => {
        console.log("cells", this.state.player.cells);
        const self = this;
        $.ajax({
            method: 'POST',
            url: "/games/create",
            data: this.state.player.cells,
            cache: false,
            success: function (result) {
                self.setState({
                    gameplay: {
                        gameId: result.gameId,
                        step: STEP_WAITING_FOR_OPPONENT,
                        currentMove: null,
                        winner: null
                    }
                });
                console.log("Game is created");
                connect();
            }
        });
    }

    onCancelGameClick = () => {
        console.log("Cancel game");
    }

    getInitialState = (state) => {
        const playerData = this.randomizeBattleFieldWithShips();
        const enemyData = this.randomizeBattleFieldWithShips();
        return {
            player: {
                playerId: null,
                playerName: 'Player',
                cells: playerData.cells,
                ships: playerData.ships,
                lastShot: null,
                damagedShipCells: [],
                points: state ? state.player.points : 0
            },
            enemy: {
                playerId: null,
                playerName: 'Enemy',
                cells: enemyData.cells,
                ships: enemyData.ships,
                lastShot: null,
                damagedShipCells: [],
                points: state ? state.enemy.points : 0
            },
            config: {
                showShotHints: state ? state.config.showShotHints : true,
                difficulty: state ? state.config.difficulty : 3, /* 1 - easy, 2 - medium, 3 - hard */
            },
            gameplay: {
                gameId: null,
                step: STEP_GAME_INIT,
                currentMove: null,
                winner: null
            },
            logs: []
        };
    }

    randomizeBattleFieldWithShips = () => {
        const cells = initBattleFieldCells(10);
        return generateShips(cells);
    }

    playerCellSetup = (cell) => {
    }

    playerShot = (cell) => {
        console.log("Player's shot", cell);
    }

    onDifficultyChange = (difficulty) => {
        this.setState({
            config: {
                showShotHints: this.state.config.showShotHints,
                difficulty: difficulty
            }
        });
    }

    onShowShotHintsChange = (e) => {
        const isShowHints = e.target.checked;
        this.setState({
            config: {
                showShotHints: isShowHints,
                difficulty: this.state.config.difficulty
            }
        });
    }

    render() {
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

        return (
            <div>
                <div className="row mt-10">
                    <div className="col-sm-1">
                        <ShipsStateRenderer
                            playerName={this.state.player.playerName}
                            ships={this.state.player.ships}
                            isPlayer={true}
                            winner={this.state.gameplay.winner}
                            points={this.state.player.points}
                        />
                    </div>
                    <div className={'col-sm-5' + (playerBattleFieldOpts.highlightBattleArea ? '' : ' disabledArea')}
                         disabled={playerBattleFieldOpts.highlightBattleArea}>
                        <BattleFieldRenderer
                            cells={this.state.player.cells}
                            options={playerBattleFieldOpts}
                            onCellClick={this.playerCellSetup}
                        />
                    </div>
                    <div className={'col-sm-5' + (enemyBattleFieldOpts.highlightBattleArea ? '' : ' disabledArea')}
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
                            points={this.state.enemy.points}
                        />
                    </div>
                </div>

                <div className="row mt-10">
                    <div className="col-sm-4">

                        <GameConfigRenderer
                            difficulty={difficulty}
                            showShotHints={this.state.config.showShotHints}
                            onDifficultyChange={this.onDifficultyChange}
                            onShowShotHintsChange={this.onShowShotHintsChange}
                        />

                    </div>
                    <div className="col-sm-4 text-center btn-lg">
                        <button
                            className="bg-primary button-rounded"
                            onClick={this.onGenerateShipsClick}
                            disabled={this.state.gameplay.step !== STEP_GAME_INIT}>
                            Ships
                        </button>
                        <button
                            className="bg-primary button-rounded"
                            onClick={this.onCreateGameClick}
                            disabled={this.state.gameplay.step !== STEP_GAME_INIT}>
                            Create game
                        </button>
                        <button
                            className="bg-primary button-rounded"
                            onClick={this.onCancelGameClick}
                            disabled={this.state.gameplay.step === STEP_GAME_INIT}>
                            Cancel game
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
        )
    }
}
