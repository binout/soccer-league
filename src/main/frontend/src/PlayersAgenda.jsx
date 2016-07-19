import React from 'react';
import {Table,Button,Badge,Label} from 'react-bootstrap';
import DatePicker from 'react-datepicker';

var moment = require('moment');
require('react-datepicker/dist/react-datepicker.css');


const PlayersAgenda = React.createClass({

    getDefaultProps() {
        return {
            players : [],
            matchDates : [],
        }
    },

    getInitialState() {
        return {
            startDate: moment()
        };
    },

    handleChange(date) {
        this.setState({
            startDate: date
        });
    },

    handleSubmit() {
        this.props.matchDateHandler(this.state.startDate);
    },

    handleOnCheck(date, player, event) {
        if (event.target.checked) {
            this.props.presentHandler(date, player);
        } else {
            this.props.absentHandler(date, player);
        }
    },

    render() {
        const thsHead = [];
        for (var i = 0; i < this.props.matchDates.length; i++) {
            const currentMatchDate = this.props.matchDates[i];
            thsHead.push(
                <th>{currentMatchDate.date}
                    &nbsp;<Badge>{currentMatchDate.presents.length}</Badge>
                    &nbsp;{this.props.matchDates[i].canBePlanned ? <Label bsStyle="success">OK</Label> : ''}
                </th>
            );
        }
        const trsBody = [];
        for (var j = 0; j < this.props.players.length; j++) {
            const cols = [];
            for (var k = 0; k < this.props.matchDates.length; k++) {
                const matchDate = this.props.matchDates[k];
                const player = this.props.players[j].name;
                cols.push(
                    <td>
                        <input type="checkbox"
                               defaultChecked={matchDate.presents.includes(player)}
                               onChange={this.handleOnCheck.bind(this, matchDate.date, player)}
                        />
                    </td>)
            }
            trsBody.push(
                <tr>
                    <td>{this.props.players[j].name}</td>
                    {cols}
                </tr>
            );
        }
        return (
            <div>
                <br/>
                <form>
                    <DatePicker selected={this.state.startDate} onChange={this.handleChange} dateFormat="YYYY/MM/DD"/>
                    &nbsp;<Button bsStyle="primary" bsSize="small" onClick={this.handleSubmit}>ADD</Button>
                </form>
                <br/>
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
            </div>
        );
    }
});

export default PlayersAgenda;