import $ from 'jquery';
import React from 'react';
import {Table,Button,Panel,Col,Glyphicon } from 'react-bootstrap';

const Players = React.createClass({

    getInitialState() {
        return {
            players : []
        }
    },

    renderLine(player) {
        const playerLeague = player.playerLeague ? <Glyphicon glyph="star"/> : '';
        const goalkeeper = player.goalkeeper ? <Glyphicon glyph="print"/> : '';
        return (
                <tr key={player.name}>
                    <td>{player.name}
                        &nbsp;{playerLeague}
                        &nbsp;{goalkeeper}
                    </td>
                    <td>{player.email}</td>
                    <td>{player.nbSeasons}</td>
                    <td>{player.nbMatches}</td>
                </tr>
            );
    },

    render() {
        const nbLeaguePlayers = this.state.players.filter(p => p.playerLeague).length
        return (
            <div>
                <h2>{this.state.players.length} Players </h2>
                <h3>{nbLeaguePlayers} League Players</h3>
                <Col md={12}>
                <Table striped bordered condensed>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Nb Seasons</th>
                        <th>Nb Matches</th>
                    </tr>
                    </thead>
                    <tbody>
                        {this.state.players.sort((p1, p2) => p2.nbMatches - p1.nbMatches).map(p => this.renderLine(p))}
                    </tbody>
                </Table>
                    <p>League Player : <Glyphicon glyph="star"/></p>
                    <p>Goalkeeper :  <Glyphicon glyph="print"/></p>
                </Col>
            </div>
        );
    },

    fetchState() {
        $.get('/rest/players-stats').done(data => this.setState({players : data}));
    },

    componentDidMount() {
       this.fetchState();
    }

});

export default Players;