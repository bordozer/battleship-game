var stompClient = null;

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
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/player2/move', function (battle) {
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
    stompClient.send("/player1/move", {}, JSON.stringify({
            'gameId': 'game-id',
            'playerId': $("input[name='player-role']:checked").val(),
            'line': 'E',
            'column': '4'
        }
    ));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.anotherPlayerMove.line + message.anotherPlayerMove.column + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMove();
    });
});
