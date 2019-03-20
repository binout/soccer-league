import React, { Component } from "react";
// import {Tabs,Tab} from 'react-bootstrap';
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
var moment = require("moment");
import PlayersAgenda from './PlayersAgenda.js';

class Agenda extends Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      players: [],
      friendlyMatchDates: [],
      leaguePlayers: [],
      leagueMatchDates: []
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(evt, value) {
    this.setState({ value });
  }

  async fetchData(url) {
    const response = await fetch(url);
    const data = await response.json();
    return data;
  }

  async componentDidMount() {
    const players = await this.fetchData("/rest/players");
    const leaguePlayers = await this.fetchData("/rest/players/league");
    this.setState({ players, leaguePlayers });
    this.fetchFriendlyMatchState();
    this.fetchLeagueMatchState();
  }

  //// LeagueMatch
  async fetchLeagueMatchState() {
    const leagueMatchDates = await this.fetchData(
      "/rest/match-dates/league/next"
    );
    this.setState({ leagueMatchDates });
  }

  //// FriendlyMatch
  async fetchFriendlyMatchState() {
    const friendlyMatchDates = await this.fetchData(
      "/rest/match-dates/friendly/next"
    );
    this.setState({ friendlyMatchDates });
  }

  async handleNewMatch(date, matchType) {
    const newMatchDate = moment(date).format("YYYY-MM-DD");
    const url = `/rest/match-dates/${matchType}/${newMatchDate}`;
    const params = {
      method: "PUT",
      contentType: "application/json",
      data: {}
    };
    const response = await fetch(url, params);
    if (response.ok) {
      if (matchType === "league") {
        this.fetchLeagueMatchState();
      } else {
        this.fetchFriendlyMatchState();
      }
    }
  }

  async handleMatchPresent(matchType, date, player) {
    const url = `/rest/match-dates/${matchType}/${date}/players/${player}`;
    const params = {
      method: "PUT",
      contentType: "application/json",
      data: {}
    };
    const response = await fetch(url, params);
    if (response.ok) {
      if (matchType === "league") {
        this.fetchLeagueMatchState();
      } else {
        this.fetchFriendlyMatchState();
      }
    }
  }

  async handleMatchAbsent(matchType, date, player) {
    const url = `/rest/match-dates/${matchType}/${date}/players/${player}`;
    const params = {
      method: "DELETE",
      contentType: "application/json",
      data: {}
    };
    const response = await fetch(url, params);
    if (response.ok) {
      if (matchType === "league") {
        this.fetchLeagueMatchState();
      } else {
        this.fetchFriendlyMatchState();
      }
    }
  }

  render() {
    const { value } = this.state;
    return (
      <div>
        <Tabs value={value} onChange={this.handleChange}>
          <Tab label="Friendly" />
          <Tab label="League" />
        </Tabs>
        {value === 0 && (
          <div>
            <PlayersAgenda
              players={this.state.players}
              matchDates={this.state.friendlyMatchDates}
              matchDateHandler={date => this.handleNewMatch(date, "friendly")}
              presentHandler={(date, player) =>
                this.handleMatchPresent("friendly", date, player)
              }
              absentHandler={(date, player) =>
                this.handleMatchAbsent("friendly", date, player)
              }
            />
          </div>
        )}
        {value === 1 && <div>League</div>}
      </div>

      // <Tabs defaultActiveKey={1} id="agenda-tab">
      //     <Tab eventKey={1} title="Friendly">
      //         <PlayersAgenda
      //             players={this.state.players}
      //             matchDates={this.state.friendlyMatchDates}
      //             matchDateHandler={date => this.handleNewMatch(date, 'friendly')}
      //             presentHandler={(date, player) => this.handleMatchPresent('friendly', date, player)}
      //             absentHandler={(date, player) => this.handleMatchAbsent('friendly', date, player)}
      //         />
      //     </Tab>
      //     <Tab eventKey={2} title="League">
      //         <PlayersAgenda
      //             players={this.state.leaguePlayers}
      //             matchDates={this.state.leagueMatchDates}
      //             matchDateHandler={date => this.handleNewMatch(date, 'league')}
      //             presentHandler={(date, player) => this.handleMatchPresent('league', date, player)}
      //             absentHandler={(date, player) => this.handleMatchAbsent('league', date, player)}
      //         />
      //     </Tab>
      // </Tabs>
    );
  }
}

export default Agenda;
