import React from "react"
import $ from "jquery"

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

    componentDidMount() {
        $.ajax({
            url: "/whoami",
            success: function (result) {
                playerId = result.playerId;
                console.log("playerId", playerId);
                connect();
            }
        });

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
                            <button id="send" className="btn btn-primary" type="submit">Move</button>
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
