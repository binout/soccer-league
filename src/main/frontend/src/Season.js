import React, { Component, Fragment } from "react";
import styled from "styled-components";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Button from "@material-ui/core/Button";
import AppBar from "@material-ui/core/AppBar";
import grey from '@material-ui/core/colors/grey';
import ScheduleMatch from './ScheduleMatch';
import Statistics from './Statistics';

var moment = require("moment");

const TabsContentWrapper = styled.div`
  margin-top: 40px;
`;
const StyledTab = styled(({ ...other }) => (
    <Tab classes={{ label: 'label' }} {...other} />
  ))` & .label {
    font-size: 14px;
    color: ${grey[900]};
  }
`
class Season extends Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      season: {
        name: ""
      },
      friendlyMatches: [],
      friendlyMatchesToPlan: [],
      leagueMatches: [],
      leagueMatchesToPlan: [],
      stats: []
    };
    this.handleChange = this.handleChange.bind(this);
    this.renderMatch = this.renderMatch.bind(this);
    this.renderMatchToPlan = this.renderMatchToPlan.bind(this);
    this.renderPlayer = this.renderPlayer.bind(this);
  }

  async componentDidMount() {
    const data = await this.fetchData("/rest/seasons/current");
    this.setState({ season: data });
    this.fetchFriendlyMatchStates();
    this.fetchLeagueMatchStates();
    // this.fetchStats();
  }

  async fetchData(url) {
    const response = await fetch(url);
    const data = await response.json();
    return data;
  }

//   async fetchStats() {
//     const stats = await this.fetchData("/rest/seasons/current/stats");
//     this.setState({ stats });
//   }

  async fetchFriendlyMatchStates() {
    const friendlyMatches = await this.fetchData(
      "/rest/seasons/current/matches/friendly/next"
    );
    const friendlyMatchesToPlan = await this.fetchData(
      "/rest/seasons/current/matches/friendly/to-plan"
    );
    this.setState({
      friendlyMatches,
      friendlyMatchesToPlan
    });
  }

  async fetchLeagueMatchStates() {
    const leagueMatches = await this.fetchData(
      "/rest/seasons/current/matches/league/next"
    );
    const leagueMatchesToPlan = await this.fetchData(
      "/rest/seasons/current/matches/league/to-plan"
    );
    this.setState({
      leagueMatches,
      leagueMatchesToPlan
    });
  }

  handleChange(evt, value) {
    this.setState({ value });
  }

  renderPlayer(match, player, substituteHandler) {
    const exit = match.subs.length !== 0 && (
      <Button size="small" onClick={() => substituteHandler(player)}>
        ❎
      </Button>
    );
    return (
      <div key={`player-${player}`}>
        <span>{player}</span>
        <span>{exit}</span>
      </div>
    );
  }

  intersperse(arr, sep) {
    if (arr.length === 0) {
      return [];
    }
    return arr.slice(1).reduce((xs, x, i) => xs.concat([sep, x]), [arr[0]]);
  }

  renderMatch(match, substituteHandler) {
    return (
      <div key={`match-${match.date}`}>
        <h4>{moment(match.date).format("dddd YYYY/MM/DD")}</h4>
        <div>
          {match.players.map(p =>
            this.renderPlayer(match, p, substituteHandler)
          )}
        </div>
        <i>Substitutes : </i>{" "}
        {match.subs.length == 0 ? "None" : this.intersperse(match.subs, ", ")}
      </div>
    );
  }

  async handleFriendlySubstitute(date, player) {
    const url = `/rest/seasons/current/matches/friendly/${date}/players/${player}`;
    const params = {
      method: "DELETE",
      contentType: "application/json",
      data: {}
    };
    const response = await fetch(url, params);
    if (response.ok) {
      this.fetchFriendlyMatchStates();
    //   this.fetchStats();
    }
  }

  async handleFriendlyPlan(date) {
    const url = `/rest/seasons/current/matches/friendly/${date}`;
    const params = {
      method: "PUT",
      contentType: "application/json",
      data: {}
    };
    const response = await fetch(url, params);
    if (response.ok) {
      this.fetchFriendlyMatchStates();
    //   this.fetchStats();
    }
  }

  async handleLeagueSubstitute(date, player) {
    const url = `/rest/seasons/current/matches/league/${date}/players/${player}`;
    const params = {
      method: "DELETE",
      contentType: "application/json",
      data: {}
    };
    const response = await fetch(url, params);
    if (response.ok) {
      this.fetchLeagueMatchStates();
    //   this.fetchStats();
    }
  }

  async handleLeaguePlan(date) {
    const url = `/rest/seasons/current/matches/league/${date}`;
    const params = {
      method: "PUT",
      contentType: "application/json",
      data: {}
    };
    const response = await fetch(url, params);
    if (response.ok) {
      this.fetchLeagueMatchStates();
    //   this.fetchStats();
    }
  }

  renderMatchToPlan(matchDate, planHanlder) {
    return (
      <li key={matchDate}>
        {matchDate.date}
        &nbsp;
        <Button color="primary" size="small" onClick={planHanlder}>
          PLAN
        </Button>
      </li>
    );
  }

  renderStatLine(stat) {
    return (
      <LineByPlayer key={stat.player}>
        <span>{stat.player}</span>
        <span>{stat.nbFriendlyMatches}</span>
        <span>{stat.nbLeagueMatches}</span>
        <span>{stat.nbMatches}</span>
      </LineByPlayer>
    );
  }

  render() {
    return (
      <div>
        <h2>Season {this.state.season.name}</h2>
        <AppBar position="static" color="default">
        <Tabs value={this.state.value} onChange={this.handleChange}>
          <StyledTab label="Friendly" />
          <StyledTab label="League" />
          <StyledTab label="Statistics" />
        </Tabs>
        </AppBar>
        <TabsContentWrapper>
        {this.state.value === 0 && <ScheduleMatch matchType="friendly"/>}
        {this.state.value === 1 && <ScheduleMatch matchType="league"/>}
        {this.state.value === 2 && <Statistics />}
        </TabsContentWrapper>
        {/* <Tabs defaultActiveKey={1} id="agenda-tab">
                    <Tab eventKey={1} title="Friendly">
                        <h3>Next friendly matches</h3>
                        <Row>
                            {this.state.friendlyMatches.map(m => this.renderMatch(m, this.handleFriendlySubstitute.bind(this, m.date)))}
                        </Row>
                        <h3>Friendly matches to plan</h3>
                        {this.state.friendlyMatchesToPlan.map(m => this.renderMatchToPlan(m, this.handleFriendlyPlan.bind(this, m.date)))}
                    </Tab>

                    <Tab eventKey={2} title="League">
                        <h3>Next league matches</h3>
                        <Row>
                            {this.state.leagueMatches.map(m => this.renderMatch(m, this.handleLeagueSubstitute.bind(this, m.date)))}
                        </Row>
                        <h3>League matches to plan</h3>
                        {this.state.leagueMatchesToPlan.map(m => this.renderMatchToPlan(m, this.handleLeaguePlan.bind(this, m.date)))}
                    </Tab>

                    <Tab eventKey={3} title="Statistics">
                        <br/>
                        <Table>
                            <thead>
                                <tr>
                                    <th>Player</th>
                                    <th>Friendly</th>
                                    <th>League</th>
                                    <th>Total Matches Played</th>
                                </tr>
                                {this.state.stats.map(s => this.renderStatLine(s))}
                            </thead>
                        </Table>
                    </Tab>
                </Tabs> */}
      </div>
    );
  }
}

export default Season;
