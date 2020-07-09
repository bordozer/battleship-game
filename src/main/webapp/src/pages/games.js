/* jshint esversion: 6 */
import React, {Component} from 'react';
import {Link} from 'react-router-dom';

export default class GamesPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            games: []
        };
    }

    componentDidMount() {
        fetch('/api/games')
            .then(response => response.json())
            .then(data => {
                this.setState({
                    games: data
                });
            });
    }

    renderGames = () => {
        const result = [];
        this.state.games.forEach(game => {
            result.push(
                <div key={game.gameId} className="row">
                    <div className="col-1" />
                    <div className="col-9">
                        <h4>
                            <Link to={"/battle?gameId=" + game.gameId}>{game.player1.name + ' vs ' + (game.player2 ? game.player2.name : '???')}</Link>
                        </h4>
                    </div>
                    <div className="col-1" />
                </div>
            )
        });

        return result;
    }

    render() {
        return (
            <div>
                <div className="row">
                    <div className="col-1" />
                    <div className="col-10">
                        {this.renderGames()}
                    </div>
                    <div className="col-1" />
                </div>
            </div>
        )
    }
}
