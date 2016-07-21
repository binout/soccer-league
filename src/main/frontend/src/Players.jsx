import $ from 'jquery';
import React from 'react';
import {Table,Button,Panel } from 'react-bootstrap';

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
            playerLeague : this.refs.inputPlayerLeague.checked
        };
        $.ajax({
            url: '/rest/players/' + name,
            type: 'PUT',
            contentType : 'application/json',
            data : JSON.stringify(player)
        }).done(data => this.fetchState());
    },

    renderLine(player) {
        return (
                <tr key={player.name}>
                    <td>{player.name}</td>
                    <td>{player.email}</td>
                    <td><input type="checkbox" checked={player.playerLeague} readOnly="true"/></td>
                </tr>
            );
    },

    render() {
        return (
            <div>
                <h2>Players</h2>
                <Table striped bordered condensed hover>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>League Player</th>
                    </tr>
                    </thead>
                    <tbody>
                        {this.state.players.map(p => this.renderLine(p))}
                    </tbody>
                </Table>
                <Panel>
                <form ref="form" onSubmit={this.handleSubmit}>
                    Name <input type="text" ref="inputPlayerName" />
                    &nbsp;Email <input type="text" ref="inputPlayerEmail" />
                    &nbsp;Plays in league : <input type="checkbox" ref="inputPlayerLeague" />
                    &nbsp;<Button bsStyle="primary" bsSize="small" onClick={this.handleSubmit}>PUT</Button>
                </form>
                </Panel>
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