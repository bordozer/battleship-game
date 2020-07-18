module.exports = function (request, response) {
    response.status(422)
        .send({
            'errors': {
                'gameId': 'b3f13569-64eb-4ada-a95e-7e868833fef9'
            }
        });
};
