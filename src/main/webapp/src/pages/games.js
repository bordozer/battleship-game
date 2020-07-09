/* jshint esversion: 6 */
import React, {Component} from 'react';
import {Link} from 'react-router-dom';

import Spinner from 'src/utils/spinner';

export default class GamesPage extends Component {

    componentDidMount() {
        fetch('/api/games')
            .then(response => response.json())
            .then(data => {
                console.log('data', data);
                this.setState({
                    playerGames: data.playerGames,
                    openGames: data.openGames
                });
            });
    }

    renderGames = (games) => {
        const result = [];
        games.forEach(game => {
            result.push(
                <div key={game.gameId} className="row">
                    <div className="col-1"/>
                    <div className="col-9">
                        <Link
                            to={'/battle?gameId=' + game.gameId}>{game.player1.name + ' vs ' + (game.player2 ? game.player2.name : '???')}
                        </Link>
                        < br />
                        <span className='small text-muted'>{game.created}</span>
                    </div>
                    <div className="col-1"/>
                </div>
            );
        });

        return result;
    };

    render() {
        if (!this.state) {
            return (
                <Spinner/>
            );
        }

        return (
            <div>

                <div className="row">
                    <div className="col-1"/>
                    <div className="col-5 text-muted text-center mt-10">
                        <h5>Your games</h5>
                    </div>
                    <div className="col-5 text-muted text-center mt-10">
                        <h5>Open games</h5>
                    </div>
                    <div className="col-1"/>
                </div>

                <div className="row">
                    <div className="col-1"/>
                    <div className="col-5 mt-10">
                        {this.renderGames(this.state.playerGames)}
                    </div>
                    <div className="col-5 mt-10">
                        {this.renderGames(this.state.openGames)}
                    </div>
                    <div className="col-1"/>
                </div>

            </div>
        );
    }
}
