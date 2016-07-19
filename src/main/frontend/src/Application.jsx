import React from 'react';
import {Router,Route,browserHistory} from 'react-router';

import SoccerNavBar from './SoccerNavBar.jsx';
import Players from './Players.jsx';
import Agenda from './Agenda.jsx';
import Season from './Season.jsx';

const Container = React.createClass({

    render() {
        const content = this.props.children == null ? <Season/> : this.props.children;
        return (
            <div className="App container">
                <SoccerNavBar/>
                {content}
            </div>
        )
    }
});

const Application = React.createClass({

    render() {
        return (
            <Router history={browserHistory}>
                <Route path="/" component={Container}>
                    <Route path="players" component={Players}/>
                    <Route path="agenda" component={Agenda}/>
                </Route>
            </Router>
        );
    },

});

export default Application;

