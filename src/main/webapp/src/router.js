/* jshint esversion: 6 */
import React from "react";
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPlusSquare} from '@fortawesome/free-regular-svg-icons';
import {faExternalLinkAlt} from '@fortawesome/free-solid-svg-icons';

import GamesPage from 'src/pages/games';
import BattlePage from 'src/pages/battle';

export default function AppRouter() {

    return (
        <Router>
            <div>
                <div className="row bg-light">

                    <div className="col-4 text-center">
                        <div className="row">
                            <div className="col-2">
                                <Link to="/battle?gameId=" style={{textDecoration: 'none'}}>
                                    <FontAwesomeIcon icon={faPlusSquare} className='fa-2x' title='Create new multiplayer game'/>
                                </Link>
                            </div>
                            <div className="col-10">
                                <Link to="/" style={{textDecoration: 'none'}}>
                                    Multiplayer games
                                </Link>
                            </div>
                        </div>

                    </div>

                    <div className="col-4 text-center">
                        Battleship game
                    </div>

                    <div className="col-4 text-center">
                        <div className="row">
                            <div className="col-10">
                                Single player
                            </div>
                            <div className="col-2">
                                <a href="https://bordozer.github.io/" target="_">
                                    <FontAwesomeIcon icon={faExternalLinkAlt} className='fa-2x' title='Open single player (external link)'/>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                {}

                <Switch>
                    <Route exact path="/">
                        <GamesPage/>
                    </Route>
                    <Route exact path="/battle">
                        <BattlePage/>
                    </Route>
                </Switch>
            </div>
        </Router>
    );
}


