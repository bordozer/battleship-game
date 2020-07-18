/* jshint esversion: 6 */
import React from 'react';
import {BrowserRouter as Router, Link, Route, Switch} from 'react-router-dom';

import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faPlusSquare} from '@fortawesome/free-regular-svg-icons';
import {faLaptop, faList} from '@fortawesome/free-solid-svg-icons';

import GamesPage from 'src/pages/games';
import BattlePage from 'src/pages/battle';

export default function AppRouter() {

    return (
        <Router>
            <div>
                <div className="row bg-light">

                    <div className="col-6 site-title">
                        <h3>
                            <Link to="/" style={{textDecoration: 'none'}}>
                                Battleship game - multiplayer
                            </Link>
                        </h3>
                    </div>

                    <div className="col-6">
                        <div className='ml-10 toolbar-buttons'>
                            <a href="https://bordozer.github.io/" target="_">
                                <FontAwesomeIcon
                                    icon={faLaptop}
                                    className='fa-2x'
                                    title='Single player game (external link, opens in a new window)'/>
                            </a>
                        </div>
                        <div className='ml-10 toolbar-buttons'>
                            <Link to="/battle?gameId=" style={{textDecoration: 'none'}}>
                                <FontAwesomeIcon
                                    icon={faPlusSquare}
                                    className='fa-2x'
                                    title='Create new multiplayer game'/>
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


