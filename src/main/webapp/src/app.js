/* jshint esversion: 6 */
import React from "react"
import ReactDOM from "react-dom"

import 'bootstrap/dist/css/bootstrap.min.css';
import './css/app.css';

import Layout from "./components/Layout"

ReactDOM.render(
    <Layout />,
    document.getElementById('js-app-context')
);
