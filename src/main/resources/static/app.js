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
    const playerId = getPlayerId();
    const anotherPlayerId = playerId === 'player1' ? 'player2' : 'player1';
    const subscribe = '/' + playerId + '/' + anotherPlayerId + '-move';

    console.log("playerId", playerId);
    console.log("anotherPlayerId", anotherPlayerId);
    console.log("subscribe", subscribe);

    const socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe(subscribe, function (battle) {
            console.log("battle", battle.body);
            // showGreeting(JSON.parse(battle.body));
            showGreeting(battle.body);
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
    const playerId = getPlayerId();
    const sendTo = "/move/" + playerId;
    console.log("sendTo", sendTo);
    stompClient.send(sendTo, {}, JSON.stringify({
            'gameId': 'game-id',
            'playerId': playerId,
            'line': 'E',
            'column': '4'
        }
    ));
}

function getPlayerId() {
    return $("input[name='player-role']:checked").val();
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
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
