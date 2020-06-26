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
                console.log("Games: ", data);
            });
    }

    renderGames = () => {
        const result = [];
        this.state.games.forEach(game => {
            result.push(
                <div key={game.gameId} className="row">
                    <div className="col-3" />
                    <div className="col-6">
                        <Link to={"/battle?gameId=" + game.gameId}>{game.gameId}</Link>
                    </div>
                    <div className="col-3" />
                </div>
            )
        });

        return result;
    }

    render() {
        return (
            <div>
                <div className="row">
                    <div className="col-3">
                        <p>
                            To play solo against AI visit<br />
                            <a href="https://bordozer.github.io/" title="Play solo">bordozer.github.io</a>
                        </p>
                    </div>
                    <div className="col-6 text-center">
                        {this.renderGames()}
                    </div>
                    <div className="col-3" />
                </div>
            </div>
        )
    }
}
