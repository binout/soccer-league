import React from "react";
import { Navbar, Nav, NavItem, Glyphicon } from "react-bootstrap";
import Paper from "@material-ui/core/Paper";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
// import {browserHistory} from 'react-router';

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
  // <Navbar>
  //     <Navbar.Header>
  //         <Navbar.Brand>
  //             <a href="/">Planning Equipe Soccer 5</a>
  //         </Navbar.Brand>
  //     </Navbar.Header>
  //     <Nav>
  //         <NavItem eventKey={1} onClick={() => browserHistory.push('agenda')}><Glyphicon glyph="calendar"/> Agenda</NavItem>
  //         <NavItem eventKey={2} onClick={() => browserHistory.push('players')}><Glyphicon glyph="user"/> Players</NavItem>
  //     </Nav>
  // </Navbar>
);

export default SoccerNavBar;
