import React, { Component } from "react";
import Button from "@material-ui/core/Button";
import Badge from "@material-ui/core/Badge";
// import { Table, Button, Label } from "react-bootstrap";
import styled from "styled-components";
import Paper from "@material-ui/core/Paper";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Checkbox from "@material-ui/core/Checkbox";
import DatePicker from "react-datepicker";

var moment = require("moment");
import "react-datepicker/dist/react-datepicker.css";

const AgendaTable = styled(Table)`
  th,
  td {
    font-size: 14px;
  }
`;
const CustomBadge = styled(Badge)`
  margin-left: 18px;
`;

class PlayersAgenda extends Component {
  constructor(props) {
    super(props);
    this.state = {
      startDate: new Date()
    };

    this.handleChange = this.handleChange.bind(this);
    this.handleOnCheck = this.handleOnCheck.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.displayMatchDateCheckBox = this.displayMatchDateCheckBox.bind(this);
  }

  handleChange(date) {
    this.setState({
      startDate: date
    });
  }

  handleSubmit() {
    this.props.matchDateHandler(this.state.startDate);
  }

  handleOnCheck(date, player, checked) {
    if (checked) {
      this.props.presentHandler(date, player);
    } else {
      this.props.absentHandler(date, player);
    }
  }

  displayMatchDateCheckBox(player) {
    return this.props.matchDates.map(matchDate => {
      return (
        <TableCell key={`date-checkbox-${player}-${matchDate.date}`}>
          <Checkbox
            type="checkbox"
            checked={matchDate.presents.includes(player)}
            onChange={(evt, checked) =>
              this.handleOnCheck(matchDate.date, player, checked)
            }
          />
        </TableCell>
      );
    });
  }

  renderMatchDate(m) {
    return (
      <TableCell key={`canbeplanneddate-${m.date}`}>
        {moment(m.date).format("dddd YYYY/MM/DD")}
        <CustomBadge
          badgeContent={m.presents.length}
          color={m.canBePlanned ? '#2e7d32':'#ffc107'}
        />
      </TableCell>
    );
  }

  //   renderThead() {
  //     return (
  //       <thead>
  //         <tr>
  //           <th>Players</th>
  //           {this.props.matchDates.map(m => this.renderMatchDate(m))}
  //         </tr>
  //       </thead>
  //     );
  //   }

  //   renderPlayerLine(player) {
  //     const checkboxes = this.props.matchDates.map(m =>
  //       this.renderMatchDateCheckbox(m, player.name)
  //     );
  //     return (
  //       <tr key={`player-line-${player.name}`}>
  //         <td>{player.name}</td>
  //         {checkboxes}
  //       </tr>
  //     );
  //   }

  //   renderTbody() {
  //     return (
  //       <tbody>{this.props.players.map(p => this.renderPlayerLine(p))}</tbody>
  //     );
  //   }

  render() {
    return (
      <div>
        <br />
        <form>
          <DatePicker
            selected={this.state.startDate}
            onChange={this.handleChange}
            dateFormat="yyyy/MM/dd"
          />
          &nbsp;
          <Button
            variant="contained"
            color="primary"
            onClick={this.handleSubmit}
          >
            ADD
          </Button>
        </form>
        <br />
        <AgendaTable>
          <TableHead>
            <TableRow>
              <TableCell>Players</TableCell>
              {this.props.matchDates.map(date => this.renderMatchDate(date))}
            </TableRow>
          </TableHead>
          <TableBody>
            {this.props.players.map(player => (
              <TableRow key={`playeragenda-${player.name}`}>
                <TableCell>{player.name}</TableCell>
                {this.displayMatchDateCheckBox(player.name)}
              </TableRow>
            ))}
          </TableBody>
        </AgendaTable>
      </div>
    );
  }
}

PlayersAgenda.defaultProps = {
  players: [],
  matchDates: []
};
export default PlayersAgenda;
