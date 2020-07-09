/* jshint esversion: 6 */
import React from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSpinner} from '@fortawesome/free-solid-svg-icons';

class Spinner extends React.Component {

    render() {
        return (
            <div>
                <div className='row'>
                    <div className='col-12 fa-10x text-muted text-center'>
                        <FontAwesomeIcon icon={faSpinner} className='fa-spin'/>
                    </div>
                </div>
            </div>
        );
    }
}

export default Spinner;
