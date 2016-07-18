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
        this.fetchFriendlyMatchState();
        $.get('/rest/players/league').done(data => this.setState({leaguePlayers : data}));
    },

    fetchLeagueMatchState() {
        $.get('/rest/match-dates/league/next').done(data => this.setState({leagueMatchDates : data}));
    },

    handleNewLeagueMatch(date) {
        const newMatchDate = date.format('YYYY-MM-DD');
        $.ajax({
            url: '/rest/match-dates/league/' + newMatchDate,
            type: 'PUT',
            contentType : 'application/json',
            data : {}
        }).done(data => this.fetchLeagueMatchState());
    },

    fetchFriendlyMatchState() {
        $.get('/rest/match-dates/friendly/next').done(data => this.setState({friendlyMatchDates : data}));
    },

    handleNewFriendlyMatch(date) {
        const newMatchDate = date.format('YYYY-MM-DD');
        $.ajax({
            url: '/rest/match-dates/friendly/' + newMatchDate,
            type: 'PUT',
            contentType : 'application/json',
            data : {}
        }).done(data => this.fetchFriendlyMatchState());
    },

    render() {

        return (
            <Tabs defaultActiveKey={1} id="agenda-tab">
                <Tab eventKey={1} title="Friendly"><PlayersAgenda
                    players={this.state.players}
                    matchDates={this.state.friendlyMatchDates}
                    matchDateHandler={this.handleNewFriendlyMatch}
                /></Tab>
                <Tab eventKey={2} title="League"><PlayersAgenda
                    players={this.state.leaguePlayers}
                    matchDates={this.state.leagueMatchDates}
                    matchDateHandler={this.handleNewLeagueMatch}/>
                </Tab>
            </Tabs>
        );
    }
});

export default Agenda;