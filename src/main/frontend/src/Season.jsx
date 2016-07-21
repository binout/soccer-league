import $ from 'jquery';
import React from 'react';
import {Button} from 'react-bootstrap';

const Season = React.createClass({

    getInitialState() {
        return {
            season : {
                name : ''
            },
            matches: [],
            matchesToPlan : []
        }
    },

    componentDidMount() {
        $.get('/rest/seasons/current').done(data => this.setState({season : data}));
        this.fetchMatchStates();
    },

    fetchMatchStates() {
        $.get('/rest/seasons/current/matches/friendly').done(data => this.setState({matches : data}));
        $.get('/rest/seasons/current/matches/friendly/to-plan').done(data => this.setState({matchesToPlan : data}));
    },

    renderMatch(match) {
        return (
            <div>
                <h4>{match.date}</h4>
                <ul>
                    {match.players.map(p => (<li>{p}</li>))}
                </ul>
                Substitutes : {match.subs}
            </div>);
    },

    handlePlan(date) {
        $.ajax({
            url: '/rest/seasons/current/matches/friendly/' + date,
            type: 'PUT',
            contentType : 'application/json',
            data : {}
        }).done(data => this.fetchMatchStates());
    },

    renderMatchToPlan(matchDate) {
        return (
            <li>
                {matchDate.date}
                &nbsp;<Button bsStyle="primary" bsSize="xsmall" onClick={() => this.handlePlan(matchDate.date)}>PLAN</Button>
            </li>
        );
    },

    render() {
        return (
            <div>
                <h2>Season {this.state.season.name}</h2>
                <h3>Next friendly matches</h3>
                {this.state.matches.map(m => this.renderMatch(m))}

                <h3>Friendly matches to plan</h3>
                {this.state.matchesToPlan.map(m => this.renderMatchToPlan(m))}

            </div>);
    }

});

export default Season;