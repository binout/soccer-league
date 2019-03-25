import React from "react";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";

const SoccerNavBar = () => (
  <Tabs
    value={this.state.value}
    onChange={this.handleChange}
    indicatorColor="primary"
    textColor="primary"
    centered
  >
    <Tab label="Planning Equipe Soccer 5" />
    <Tab label="Agenda" />
    <Tab label="Players" />
  </Tabs>
);

export default SoccerNavBar;
