import React from "react"
import $ from "jquery"

const StompJs = require('@stomp/stompjs');

const playerId = String(new Date().getTime());
let stompClient = null;

function connect() {
    const ws = 'ws://localhost:8036/gs-guide-websocket'; /* Todo: move URL to config */
    stompClient = new StompJs.Client({
        brokerURL: ws,
        connectHeaders: {},
        debug: function (str) {
            console.log(str);
        },
        reconnectDelay: 50000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000
    });
    stompClient.onConnect = function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/outbound', function (battle) {
            console.log("battle", battle.body);
            showGreeting(JSON.parse(battle.body));
        });
    };
    stompClient.onStompError = function (frame) {
        console.log('Broker reported error: ' + frame.headers['message']);
        console.log('Additional details: ' + frame.body);
    };
    stompClient.activate();
}

function sendMove() {
    stompClient.send("/inbound", {}, JSON.stringify({
            'gameId': 'game-id',
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
    $("#greetings").append("<tr><td>" + message.playerMove.line + message.playerMove.column + "</td></tr>");
}

/*function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}*/

export default class Layout extends React.Component {

    componentDidMount() {
        connect();

        $(function () {
            $("form").on('submit', function (e) {
                e.preventDefault();
            });
            $("#send").click(function () {
                sendMove();
            });
        });
    }

    render() {
        return <div className="row">
            <div className="col-12">
                <div className="row">
                    <div className="col-md-6">
                        <form className="form-inline">
                            <button id="send" className="btn btn-default" type="submit">Move</button>
                        </form>
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-12">
                        <table id="conversation" className="table table-striped">
                            <thead>
                            <tr>
                                <th>Moves</th>
                            </tr>
                            </thead>
                            <tbody id="greetings">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    }
}
