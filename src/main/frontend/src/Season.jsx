import $ from 'jquery';
import React from 'react';

const Season = React.createClass({

    getInitialState() {
        return {
            season : {
                name : ''
            },
            matches: []
        }
    },

    componentDidMount() {
        $.get('/rest/seasons/current').done(data => this.setState({season : data}));
        $.get('/rest/seasons/current/matches/friendly').done(data => this.setState({matches : data}));
    },

    renderMatch(match) {
        return (
            <div>
                <h4>{match.date}</h4>
                <ul>
                    {match.players.map(p => (<li>{p}</li>))}
                </ul>
            </div>);
    },

    render() {
        return (
            <div>
                <h2>Season {this.state.season.name}</h2>
                <h3>Next friendly matches</h3>
                {this.state.matches.map(m => this.renderMatch(m))}
            </div>);
    }

});

export default Season;