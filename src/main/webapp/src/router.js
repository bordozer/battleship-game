/* jshint esversion: 6 */
import React from "react";
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPlusSquare} from '@fortawesome/free-regular-svg-icons';

import GamesPage from 'src/pages/games';
import BattlePage from 'src/pages/battle';

export default function AppRouter() {

    return (
        <Router>
            <div>
                <div className="row bg-light">
                    <div className="col-8 fa-2x">
                        <Link to="/" style={{textDecoration: 'none'}}>
                            Battleship game - multiplayer
                        </Link>
                    </div>
                    <div className="col-4">
                        <div className="float-right">
                            <Link to="/battle?gameId=" style={{textDecoration: 'none'}}>
                                <FontAwesomeIcon icon={faPlusSquare} className='fa-2x' title='Create new game'/>
                            </Link>
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


