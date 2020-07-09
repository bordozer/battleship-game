/* jshint esversion: 6 */

function spawnNotification(gameId, notificationText) {
    const options = {
        body: 'Battleship game',
        tag: 'battle'
    };
    const n = new Notification(notificationText, options);
    n.onclick = function(event) {
        event.preventDefault();
        window.open('/battle?gameId=' + gameId, '_blank');
    }
    setTimeout(() => {
        n.close();
    }, 3000);
}

export const showNotification = (gameId, notification) => {
    if (!('Notification' in window)) {
        return;
    }

    const notificationText = notification.messages.join('\n');

    if (Notification.permission === 'granted') {
        spawnNotification(gameId, notificationText);
        return;
    }
    if (Notification.permission !== 'denied') {
        Notification.requestPermission()
            .then(function (permission) {
                if (permission === 'granted') {
                    spawnNotification(gameId, notificationText);
                }
            });
    }
};
