const playerId = String(new Date().getTime());
let stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/outbound', function (battle) {
            console.log("battle", battle.body);
            showGreeting(JSON.parse(battle.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
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
    return playerId; //$("input[name='player-role']:checked").val();
}

function showGreeting(message) {
    const playerId = getPlayerId();
    if (message.playerMove.playerId === playerId) {
        return;
    }
    $("#greetings").append("<tr><td>" + message.playerMove.line + message.playerMove.column + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    /*$("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });*/
    $("#send").click(function () {
        sendMove();
    });
});
$( document ).ready(function() {
    connect();
});
