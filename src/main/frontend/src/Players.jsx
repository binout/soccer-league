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
        var playerLeague = player.playerLeague ? <Glyphicon glyph="star"/> : '';
        var goalkeeper = player.goalkeeper ? <Glyphicon glyph="print"/> : '';
        return (
                <tr key={player.name}>
                    <td>{player.name}
                        &nbsp;{playerLeague}
                        &nbsp;{goalkeeper}
                    </td>
                    <td>{player.email}</td>
                </tr>
            );
    },

    render() {
        return (
            <div>
                <h2>Players</h2>
                <Col md={12}>
                <Table striped bordered condensed>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                    </tr>
                    </thead>
                    <tbody>
                        {this.state.players.map(p => this.renderLine(p))}
                    </tbody>
                </Table>
                    <p>League Player : <Glyphicon glyph="star"/></p>
                    <p>Goalkeeper :  <Glyphicon glyph="print"/></p>
                </Col>
            </div>
        );
    },

    fetchState() {
        $.get('/rest/players').done(data => this.setState({players : data}));
    },

    componentDidMount() {
       this.fetchState();
    }

});

export default Players;