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
        fetch('/games')
            .then(response => response.json())
            .then(data => {
                this.setState({
                    games: data
                });
                // console.log("Games: ", data);
            });
    }

    renderGames = () => {
        const result = [];
        this.state.games.forEach(game => {
            result.push(
                <div key={game.gameId} className="row">
                    <div className="col-12">
                        <Link to={"/battle?gameId=" + game.gameId}>{game.gameId}</Link>
                    </div>
                </div>
            )
        });

        return result;
    }

    render() {
        return (
            <div>
                <div className="row">
                    <div className="col-12">
                        <Link to="/battle?gameId=">New game</Link>
                    </div>
                </div>
                {this.renderGames()}
            </div>
        )
    }
}