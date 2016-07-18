import React from 'react';
import {Table,Button} from 'react-bootstrap';
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