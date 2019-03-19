import React from 'react';
import {Button,Glyphicon,Row,Col,Tabs,Tab,Table} from 'react-bootstrap';

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

    async componentDidMount() {
        const data = await this.fetchData('/rest/seasons/current');
        this.setState({season : data});
        this.fetchFriendlyMatchStates();
        this.fetchLeagueMatchStates();
        this.fetchStats();
    },

    async fetchData(url){
        const response = await fetch(url);
        const data = await response.json();
        return data;
    },

    async fetchStats() {
        const stats = await this.fetchData('/rest/seasons/current/stats');
        this.setState({stats});
    },

    async fetchFriendlyMatchStates() {
        const friendlyMatches = await this.fetchData('/rest/seasons/current/matches/friendly/next');
        const friendlyMatchesToPlan = await this.fetchData('/rest/seasons/current/matches/friendly/to-plan');
        this.setState({
            friendlyMatches, friendlyMatchesToPlan
        })
    },

    async fetchLeagueMatchStates() {
        const leagueMatches = await this.fetchData('/rest/seasons/current/matches/league/next');
        const leagueMatchesToPlan = await this.fetchData('/rest/seasons/current/matches/league/to-plan');
        this.setState({
            leagueMatches, leagueMatchesToPlan
        })
    },

    renderPlayer(match, player, substituteHandler) {
        const exit = match.subs.length == 0
            ? ''
            : <Button bsSize="xsmall" onClick={() => substituteHandler(player)}><Glyphicon glyph="log-out"/></Button>;
        return (
            <tr key={`player-${player}`}>
                <td>{player}</td>
                <td>{exit}</td>
            </tr>);
    },

    intersperse(arr, sep) {
        if (arr.length === 0) {
            return [];
        }
        return arr.slice(1).reduce((xs, x, i) => xs.concat([sep, x]), [arr[0]]);
    },

    renderMatch(match, substituteHandler) {
        return (
            <div key={`match-${match.date}`}>
                <Col md={3}>
                    <h4>{moment(match.date).format('dddd YYYY/MM/DD')}</h4>
                    <Table condensed>
                        <tbody>
                        {match.players.map(p => this.renderPlayer(match, p, substituteHandler))}
                        </tbody>
                    </Table>
                    <i>Substitutes : </i> {match.subs.length == 0 ? 'None' : this.intersperse(match.subs, ", ")}
                </Col>
            </div>);
    },

    async handleFriendlySubstitute(date, player) {
        const url = `/rest/seasons/current/matches/friendly/${date}/players/${player}`;
        const params = {
            method: 'DELETE',
            contentType : 'application/json',
            data : {}
        }
        const response = await fetch(url, params);
        if(response.ok){
            this.fetchFriendlyMatchStates();
            this.fetchStats()
        }
    },

    async handleFriendlyPlan(date) {
        const url = `/rest/seasons/current/matches/friendly/${date}`;
        const params = {
            method: 'PUT',
            contentType : 'application/json',
            data : {}
        }
        const response = await fetch(url, params)
        if(response.ok){
            this.fetchFriendlyMatchStates();
            this.fetchStats()
        }
    },

    async handleLeagueSubstitute(date, player) {
        const url = `/rest/seasons/current/matches/league/${date}/players/${player}`;
        const params = {
            method: 'DELETE',
            contentType : 'application/json',
            data : {}
        }
        const response = await fetch(url, params);
        if(response.ok){
            this.fetchLeagueMatchStates();
            this.fetchStats()
        }
    },

    async handleLeaguePlan(date) {
        const url = `/rest/seasons/current/matches/league/${date}`;
        const params = {
            method: 'PUT',
            contentType : 'application/json',
            data : {}
        }
        const response = await fetch(url, params);
        if(response.ok){
            this.fetchLeagueMatchStates();
            this.fetchStats()
        }
    },

    renderMatchToPlan(matchDate, planHanlder) {
        return (
            <li key={matchDate}>
                {matchDate.date}
                &nbsp;<Button bsStyle="primary" bsSize="xsmall" onClick={planHanlder}>PLAN</Button>
            </li>
        );
    },

    renderStatLine(stat) {
        return (
          <tr key={stat.player}>
              <td>{stat.player}</td>
              <td>{stat.nbFriendlyMatches}</td>
              <td>{stat.nbLeagueMatches}</td>
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
                            {this.state.friendlyMatches.map(m => this.renderMatch(m, this.handleFriendlySubstitute.bind(this, m.date)))}
                        </Row>
                        <h3>Friendly matches to plan</h3>
                        {this.state.friendlyMatchesToPlan.map(m => this.renderMatchToPlan(m, this.handleFriendlyPlan.bind(this, m.date)))}
                    </Tab>

                    <Tab eventKey={2} title="League">
                        <h3>Next league matches</h3>
                        <Row>
                            {this.state.leagueMatches.map(m => this.renderMatch(m, this.handleLeagueSubstitute.bind(this, m.date)))}
                        </Row>
                        <h3>League matches to plan</h3>
                        {this.state.leagueMatchesToPlan.map(m => this.renderMatchToPlan(m, this.handleLeaguePlan.bind(this, m.date)))}
                    </Tab>

                    <Tab eventKey={3} title="Statistics">
                        <br/>
                        <Table>
                            <thead>
                                <tr>
                                    <th>Player</th>
                                    <th>Friendly</th>
                                    <th>League</th>
                                    <th>Total Matches Played</th>
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