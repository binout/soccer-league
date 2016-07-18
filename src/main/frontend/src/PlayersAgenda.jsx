import React from 'react';
import {Table} from 'react-bootstrap';

const PlayersAgenda = React.createClass({

    getDefaultProps() {
        return {
            players : [],
            matchDates : []
        }
    },

    render() {
        const thsHead = [];
        for (var i = 0; i < this.props.matchDates.length; i++) {
            thsHead.push(
                <th>{this.props.matchDates[i].date}</th>
            );
        }
        const trsBody = [];
        for (var j = 0; j < this.props.players.length; j++) {
            const cols = [];
            for (var k = 0; k < this.props.matchDates.length; k++) {
                cols.push(<td><input type="checkbox"/></td>)
            }
            trsBody.push(
                <tr>
                    <td>{this.props.players[j].name}</td>
                    {cols}
                </tr>
            );
        }
        return (
            <Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th>Players</th>
                    {thsHead}
                </tr>
                </thead>
                <tbody>
                {trsBody}
                </tbody>
            </Table>
        );
    }
});

export default PlayersAgenda;