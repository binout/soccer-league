import $ from 'jquery';
import React from 'react';
import {Router,Route,browserHistory} from 'react-router';

import SoccerNavBar from './SoccerNavBar.jsx';
import Players from './Players.jsx';
import Agenda from './Agenda.jsx';

const Welcome = React.createClass({

    render() {
        return (<p>Welcome to Planning for Soccer5 League</p>);
    }
});

const Container = React.createClass({

    getInitialState() {
        return {
            season : {}
        }
    },

    componentDidMount() {
        $.get('/rest/seasons/current').done(data => this.setState({season : data}));
    },

    render() {
        const content = this.props.children == null ? <Welcome/> : this.props.children;
        return (
            <div className="App container">
                <SoccerNavBar season={this.state.season}/>
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

