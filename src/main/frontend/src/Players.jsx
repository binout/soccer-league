import $ from 'jquery';
import React from 'react';
import { Table } from 'react-bootstrap';

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

    renderLines() {
        const lines = [];
        for (var i = 0; i < this.state.players.length; i++) {
            lines.push(
                <tr key={i}>
                    <td>{this.state.players[i].name}</td>
                    <td>{this.state.players[i].email}</td>
                    <td><input type="checkbox" defaultChecked={this.state.players[i].playerLeague}/></td>
                </tr>
            )
        }
        return lines;
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
                        {this.renderLines()}
                    </tbody>
                </Table>
                <form ref="form" onSubmit={this.handleSubmit}>
                    Name : <input type="text" ref="inputPlayerName" />
                    Email : <input type="text" ref="inputPlayerEmail" />
                    Plays in league : <input type="checkbox" ref="inputPlayerLeague" />
                    <button onClick={this.handleSubmit}>PUT</button>
                </form>
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