import React, { Component } from "react";
import styled from "styled-components";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import AppBar from "@material-ui/core/AppBar";
import grey from '@material-ui/core/colors/grey';
import ScheduleMatch from './ScheduleMatch';
import Statistics from './Statistics';


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
      }
    };
    this.handleChange = this.handleChange.bind(this);
  }

  async componentDidMount() {
    const data = await this.fetchData("/rest/seasons/current");
    this.setState({ season: data });
  }

  async fetchData(url) {
    const response = await fetch(url);
    const data = await response.json();
    return data;
  }



  handleChange(evt, value) {
    this.setState({ value });
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
      </div>
    );
  }
}

export default Season;
