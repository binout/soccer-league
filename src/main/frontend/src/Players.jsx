import React, {Component} from 'react';
import {Table,Col,Glyphicon } from 'react-bootstrap';

class Players extends Component{
    
    constructor(props){
        super(props);
        this.state = {
            players: []
        }
        this.renderLine = this.renderLine.bind(this);
        this.fetchState = this.fetchState.bind(this);
    }

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
                </tr>
            );
    }

    async fetchState() {
        const response = await fetch('/rest/players');
        const players = await response.json();
        this.setState({players})
    }

    componentDidMount() {
       this.fetchState();
    }

    render() {
        const nbLeaguePlayers = this.state.players.filter(p => p.playerLeague).length
        return (
            <div>
                <h2>{this.state.players.length} Players </h2>
                <h3>{nbLeaguePlayers} League Players</h3>
                {/* <Col md={12}>
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
                </Col> */}
            </div>
        );
    }

};

export default Players;