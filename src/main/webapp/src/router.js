/* jshint esversion: 6 */
import React from "react";
import {BrowserRouter as Router, Link, Route, Switch, useRouteMatch} from "react-router-dom";

import GamesPage from 'src/pages/games';
import BattlePage from 'src/pages/battle';

export default function AppRouter() {

    return (
        <Router>
            <div>

                <div className="row bg-light">
                    <div className="col-sm-12 text-center">
                        <OldSchoolMenuLink
                            activeOnlyWhenExact={true}
                            to="/"
                            label="Game list"
                        />
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

function OldSchoolMenuLink({label, to, activeOnlyWhenExact}) {
    let match = useRouteMatch({
        path: to,
        exact: activeOnlyWhenExact
    });

    return (
        <div>
            <Link
                to={to}
                style={{textDecoration: 'none'}}
                className={match ? "active text-danger" : "text-dark"}
            >
                {label}
            </Link>
        </div>
    );
}


