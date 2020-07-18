/* jshint esversion: 6 */
import React, {Component} from 'react';
import {Link} from 'react-router-dom';

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSignInAlt} from '@fortawesome/free-solid-svg-icons';

import Spinner from 'src/utils/spinner';

export default class GamesPage extends Component {

    componentDidMount() {
        fetch('/api/whoami')
            .then(response => response.json())
            .then(data => {
                const player = data.player;

                fetch('/api/games')
                    .then(response => response.json())
                    .then(data => {
                        console.log('data', data);
                        this.setState({
                            player: player,
                            playerGames: data.playerGames,
                            openGames: data.openGames
                        });
                    });
            });
    }

    renderPlayer = (player) => {
        const result = [];
        if (!player) {
            result.push(
                <span className='text-muted'>???</span>
            );
            return result;
        }
        const me = this.state.player;
        if (player.id === me.id) {
            result.push(
                <span className='text-dark'><strong>{player.name}</strong></span>
            );
        } else {
            result.push(
                <span className='text-dark'>{player.name}</span>
            );
        }

        return result;
    };

    renderGames = (games) => {
        const result = [];
        games.forEach(game => {
            result.push(
                <div key={game.gameId} className="row">
                    <div className="col-1"/>
                    <div className="col-9">

                        <div className='row game-box'>
                            <div className="col-2">
                                <Link to={'/battle?gameId=' + game.gameId}>
                                    <FontAwesomeIcon icon={faSignInAlt} className='fa-3x' title='Enter the game'/>
                                </Link>
                            </div>
                            <div className="col-10">
                                <div className='row'>
                                    <div className="col-6">
                                        {this.renderPlayer(game.player1)}
                                    </div>
                                    <div className="col-6">
                                        {this.renderPlayer(game.player2)}
                                    </div>
                                </div>
                                <div className='row'>
                                    <div className="col-12">
                                        <span className='small text-muted'>{game.created}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
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
