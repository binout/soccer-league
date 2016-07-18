import $ from 'jquery';
import React from 'react';
import {Table,Tabs,Tab} from 'react-bootstrap';

import PlayersAgenda from './PlayersAgenda.jsx';

const Agenda = React.createClass({

    getInitialState() {
        return {
            players : [],
            friendlyMatchDates : [],
            leaguePlayers : [],
            leagueMatchDates : []
        }
    },

    componentDidMount() {
        $.get('/rest/players').done(data => this.setState({players : data}));
        $.get('/rest/match-dates/friendly/next').done(data => this.setState({friendlyMatchDates : data}));
        $.get('/rest/players/league').done(data => this.setState({leaguePlayers : data}));
        $.get('/rest/match-dates/league/next').done(data => this.setState({leagueMatchDates : data}));
    },

    render() {

        return (
            <Tabs defaultActiveKey={1} id="agenda-tab">
                <Tab eventKey={1} title="Friendly"><PlayersAgenda players={this.state.players} matchDates={this.state.friendlyMatchDates}/></Tab>
                <Tab eventKey={2} title="League"><PlayersAgenda players={this.state.leaguePlayers} matchDates={this.state.leagueMatchDates}/></Tab>
            </Tabs>
        );
    }
});

export default Agenda;