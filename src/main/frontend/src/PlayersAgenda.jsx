import React from 'react';
import {Table,Button,Label} from 'react-bootstrap';
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

    renderMatchDate(m) {
        return (
            <th>{moment(m.date).format('dddd YYYY/MM/DD')}
                &nbsp;<Label bsStyle={m.canBePlanned ? "success" : "warning"}>{m.presents.length}</Label>
            </th>
        );
    },

    renderMatchDateCheckbox(matchDate, player) {
        return (
            <td>
                <input type="checkbox"
                       defaultChecked={matchDate.presents.includes(player)}
                       onChange={this.handleOnCheck.bind(this, matchDate.date, player)}
                />
            </td>
        );
    },

    renderThead() {
        return (
            <thead>
            <tr>
                <th>Players</th>
                {this.props.matchDates.map(m => this.renderMatchDate(m))}
            </tr>
            </thead>
        );
    },

    renderPlayerLine(player) {
        const checkboxes = this.props.matchDates.map(m => this.renderMatchDateCheckbox(m, player.name));
        return (
            <tr>
                <td>{player.name}</td>
                {checkboxes}
            </tr>
        );
    },

    renderTbody() {
        return (
            <tbody>
            {this.props.players.map(p => this.renderPlayerLine(p))}
            </tbody>
        );
    },

    render() {
        return (
            <div>
                <br/>
                <form>
                    <DatePicker selected={this.state.startDate} onChange={this.handleChange} dateFormat="YYYY/MM/DD"/>
                    &nbsp;<Button bsStyle="primary" bsSize="small" onClick={this.handleSubmit}>ADD</Button>
                </form>
                <br/>
                <Table striped bordered condensed>
                    {this.renderThead()}
                    {this.renderTbody()}
                </Table>
            </div>
        );
    }
});

export default PlayersAgenda;