import $ from 'jquery';
import React from 'react';
import {Table,Button,Panel,Col,Glyphicon } from 'react-bootstrap';

const Players = React.createClass({

    getInitialState() {
        return {
            players : []
        }
    },

    handleSubmit(e){
        e.preventDefault();
        var name = this.refs.inputPlayerName.value;
        var player = {
            name : name,
            email : this.refs.inputPlayerEmail.value,
            playerLeague : this.refs.inputPlayerLeague.checked,
            goalkeeper : this.refs.inputGoalkeeper.checked
        };
        $.ajax({
            url: '/rest/players/' + name,
            type: 'PUT',
            contentType : 'application/json',
            data : JSON.stringify(player)
        }).done(data => this.fetchState());
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
                <Col md={9}>
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
                <Col md={3}>
                <Panel>
                <form ref="form" onSubmit={this.handleSubmit}>
                    Name <input type="text" ref="inputPlayerName" /> <br/><br/>
                    Email <input type="text" ref="inputPlayerEmail" /> <br/><br/>
                    Plays in league : <input type="checkbox" ref="inputPlayerLeague" /> <br/><br/>
                    Plays as goalkeeper : <input type="checkbox" ref="inputGoalkeeper" /> <br/><br/>
                    <Button bsStyle="primary" bsSize="small" onClick={this.handleSubmit}>PUT</Button>
                </form>
                </Panel>
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