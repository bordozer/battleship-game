/* jshint esversion: 6 */

function spawnNotification(notificationText) {
    const options = {
        body: 'Battleship game',
        tag: 'battle'
    };
    const n = new Notification(notificationText, options);
    setTimeout(() => {
        n.close();
    }, 3000);
}

export const showNotification = (notification) => {
    if (!('Notification' in window)) {
        return;
    }

    const notificationText = notification.messages.join('\n');

    if (Notification.permission === 'granted') {
        spawnNotification(notificationText);
        return;
    }
    if (Notification.permission !== 'denied') {
        Notification.requestPermission()
            .then(function (permission) {
                if (permission === 'granted') {
                    spawnNotification(notificationText);
                }
            });
    }
};
