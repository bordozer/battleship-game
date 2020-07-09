/* jshint esversion: 6 */
import Swal from 'sweetalert2';
import $ from 'jquery';

export const cancelGame = (gameId, onCancelCallback) => {
    Swal.fire({
        title: 'Cancel the game?',
        text: "It will look like you gave up!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, cancel it!'
    }).then((result) => {
        if (!result.value) {
            return;
        }
        $.ajax({
            method: 'DELETE',
            url: '/api/games/delete/' + gameId,
            contentType: 'application/json',
            cache: false,
            success: function (result) {
                onCancelCallback();
            },
            error: function (request, status, error) {
                console.error('Cannot delete game', request.responseText, error);
            }
        });
    })
};
