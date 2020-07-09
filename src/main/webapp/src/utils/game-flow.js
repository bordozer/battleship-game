/* jshint esversion: 6 */
import Swal from 'sweetalert2';
import $ from 'jquery';

export const createGame = (cells, callback) => {
    $.ajax({
        method: 'POST',
        url: '/api/games/create',
        contentType: 'application/json',
        data: JSON.stringify(cells),
        cache: false,
        success: function (result) {
            callback(result.gameId);
        }
    });
};

export const joinGame = (gameId, cells, callback) => {
    $.ajax({
        method: 'PUT',
        url: '/api/games/join/' + gameId,
        contentType: 'application/json',
        data: JSON.stringify(cells),
        cache: false,
        success: function () {
            callback();
        },
        error: function (request, status, error) {
            console.error('Cannot join game', request.responseText, error);
        }
    });
};

export const cancelGame = (gameId, beforeDeleteCallback, afterDeleteCallback) => {
    Swal.fire({
        title: 'Cancel the game?',
        text: 'It will look like you gave up!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, cancel it!'
    })
        .then((result) => {
            if (!result.value) {
                return;
            }
            beforeDeleteCallback();
            $.ajax({
                method: 'DELETE',
                url: '/api/games/delete/' + gameId,
                contentType: 'application/json',
                cache: false,
                success: function (result) {
                    afterDeleteCallback();
                },
                error: function (request, status, error) {
                    console.error('Cannot delete game', request.responseText, error);
                }
            });
        });
};
