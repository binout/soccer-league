import $ from 'jquery';
import React from 'react';
import {Button,Row,Col,Tabs,Tab,Table} from 'react-bootstrap';

var moment = require('moment');

const Season = React.createClass({

    getInitialState() {
        return {
            season : {
                name : ''
            },
            friendlyMatches: [],
            friendlyMatchesToPlan : [],
            leagueMatches: [],
            leagueMatchesToPlan : [],
            stats : []
        }
    },

    componentDidMount() {
        $.get('/rest/seasons/current').done(data => this.setState({season : data}));
        this.fetchFriendlyMatchStates();
        this.fetchLeagueMatchStates();
        this.fetchStats();
    },

    fetchStats() {
        $.get('/rest/seasons/current/stats').done(data => this.setState({stats : data}));
    },

    fetchFriendlyMatchStates() {
        $.get('/rest/seasons/current/matches/friendly').done(data => this.setState({friendlyMatches : data}));
        $.get('/rest/seasons/current/matches/friendly/to-plan').done(data => this.setState({friendlyMatchesToPlan : data}));
    },

    fetchLeagueMatchStates() {
        $.get('/rest/seasons/current/matches/league').done(data => this.setState({leagueMatches : data}));
        $.get('/rest/seasons/current/matches/league/to-plan').done(data => this.setState({leagueMatchesToPlan : data}));
    },

    renderMatch(match) {
        return (
            <div>
                <Col md={3}>
                    <h4>{moment(match.date).format('dddd YYYY/MM/DD')}</h4>
                    <ul>
                        {match.players.map(p => (<li>{p}</li>))}
                    </ul>
                    Substitutes : {match.subs}
                </Col>
            </div>);
    },

    handleFriendlyPlan(date) {
        $.ajax({
            url: '/rest/seasons/current/matches/friendly/' + date,
            type: 'PUT',
            contentType : 'application/json',
            data : {}
        }).done(data => {this.fetchFriendlyMatchStates();this.fetchStats()});
    },

    handleLeaguePlan(date) {
        $.ajax({
            url: '/rest/seasons/current/matches/league/' + date,
            type: 'PUT',
            contentType : 'application/json',
            data : {}
        }).done(data => {this.fetchLeagueMatchStates();this.fetchStats()});
    },

    renderMatchToPlan(matchDate, planHanlder) {
        return (
            <li>
                {matchDate.date}
                &nbsp;<Button bsStyle="primary" bsSize="xsmall" onClick={planHanlder}>PLAN</Button>
            </li>
        );
    },

    renderStatLine(stat) {
        return (
          <tr>
              <td>{stat.player}</td>
              <td>{stat.nbMatches}</td>
          </tr>
        );
    },

    render() {
        return (
            <div>
                <h2>Season {this.state.season.name}</h2>
                <Tabs defaultActiveKey={1} id="agenda-tab">
                    <Tab eventKey={1} title="Friendly">
                        <h3>Next friendly matches</h3>
                        <Row>
                            {this.state.friendlyMatches.map(m => this.renderMatch(m))}
                        </Row>
                        <h3>Friendly matches to plan</h3>
                        {this.state.friendlyMatchesToPlan.map(m => this.renderMatchToPlan(m, this.handleFriendlyPlan.bind(this, m.date)))}
                    </Tab>

                    <Tab eventKey={2} title="League">
                        <h3>Next league matches</h3>
                        <Row>
                            {this.state.leagueMatches.map(m => this.renderMatch(m))}
                        </Row>
                        <h3>League matches to plan</h3>
                        {this.state.leagueMatchesToPlan.map(m => this.renderMatchToPlan(m, this.handleLeaguePlan.bind(this, m.date)))}
                    </Tab>

                    <Tab eventKey={3} title="Statistics">
                        <Table>
                            <thead>
                                <tr>
                                    <th>Player</th>
                                    <th>Matches Played</th>
                                </tr>
                                {this.state.stats.map(s => this.renderStatLine(s))}
                            </thead>
                        </Table>
                    </Tab>
                </Tabs>
            </div>);
    }

});

export default Season;