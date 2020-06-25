/* jshint esversion: 6 */
import React from 'react';

function renderLogs(logs) {
    const result = [];
    let counter = 0;
    logs.forEach(rec => {
        result.push(
            <div key={'log-row-' + counter} className="row">
                <div key={'log-row-col-' + counter} className="col-sm-12 small text-muted">
                    {rec.text}
                </div>
            </div>
        );
        counter++;
    });
    return result;
}

const LogsRenderer = ({logs}) => {
    return (
        <div className="logs-container">
            {renderLogs(logs)}
        </div>
    )
}

export default LogsRenderer;
