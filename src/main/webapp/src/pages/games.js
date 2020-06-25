/* jshint esversion: 6 */
import React, {Component} from 'react';
import { Link } from 'react-router-dom';

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
                console.log("Games: ", data);
            });
    }

    render() {
        return (
            <div className="row">
                <div className="col-12">
                    <Link to="/battle?gameId=">New game</Link>
                </div>
                <div className="col-12">
                    <Link to="/battle?gameId=8327de36-5a26-4034-a032-e7bc6b221084">8327de36-5a26-4034-a032-e7bc6b221084</Link>
                </div>
            </div>
        )
    }
}
