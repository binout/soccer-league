import React, { Component } from "react";
import styled from "styled-components";
import Paper from "@material-ui/core/Paper";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";

const PlayersWrapper = styled.div`
  display: flex;
  flex-direction: column;
`;

const PlayersTable = styled(Table)`
  th,
  td {
    font-size: 14px;
  }
`;
const CustomTableHead = styled(TableHead)`
  background-color: black;
  th {
    color: white;
  }
`;

const TitleWrapper = styled.div`
margin-left: 20px;
  h2: {
    font-size: 30px;
  }
  h3: {
    font-size: 24px;
  }
`;

const Note = styled.div`
  align-self: flex-end;
  margin: 0 20px 5px 0;
`;

class Players extends Component {
  constructor(props) {
    super(props);
    this.state = {
      players: []
    };
    this.fetchState = this.fetchState.bind(this);
  }

  async fetchState() {
    const response = await fetch("/rest/players");
    const players = await response.json();
    this.setState({ players });
  }

  componentDidMount() {
    this.fetchState();
  }

  render() {
    const nbLeaguePlayers = this.state.players.filter(p => p.playerLeague)
      .length;
    return (
      <PlayersWrapper>
        <TitleWrapper>
          <h2>{this.state.players.length} Players </h2>
          <h3>{nbLeaguePlayers} League Players</h3>
        </TitleWrapper>
        <Note>
          <span>League Player: "⭐"</span>
          <span>Goalkeeper: "🥅"</span>
        </Note>
        <PlayersTable>
          <CustomTableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Email</TableCell>
            </TableRow>
          </CustomTableHead>
          <TableBody>
            {this.state.players.map(player => (
              <TableRow key={player.name}>
                <TableCell>
                  {player.name} {player.playerLeague && "⭐"}
                  {player.goalkeeper && " 🥅"}
                </TableCell>
                <TableCell>{player.email}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </PlayersTable>
      </PlayersWrapper>
    );
  }
}

export default Players;
