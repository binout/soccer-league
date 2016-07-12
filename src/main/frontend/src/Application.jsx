import $ from 'jquery';
import React from 'react';

import SoccerNavBar from './SoccerNavBar.jsx';
import Players from './Players.jsx';


const Application = React.createClass({

    getInitialState() {
        return {
            seasons : []
        }
    },

    render() {
        return (
            <div className="App container">
               <SoccerNavBar seasons={this.state.seasons}/>
                <Players/>
            </div>
        );
    },

    componentDidMount() {
        $.get('/seasons').done(data => this.setState({seasons : data.map(s => s.name)}));
    }

});

export default Application;

