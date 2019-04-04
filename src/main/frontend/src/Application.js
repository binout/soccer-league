import React from "react";
import { BrowserRouter as Router, Route, NavLink } from "react-router-dom";
import styled from "styled-components";
import { createGlobalStyle } from "styled-components";
import AppBar from "@material-ui/core/AppBar";
import green from "@material-ui/core/colors/green";
import Players from "./Players.js";
import Agenda from "./Agenda.js";
import Season from "./Season.js";
import { Toolbar } from "@material-ui/core";
import { media } from "./style";

const GlobalStyle = createGlobalStyle`
  body {
    margin: 0;
    font-family: "Roboto", "Helvetica", "Arial", sans-serif;
  }
`;

const SoccerAppWrapper = styled.div`
  margin: 0 auto;
`;

const StyledAppBar = styled(AppBar)`
  && {
    background-color: ${green[900]};
    margin-bottom: 50px;
    a {
      color: white;
      text-decoration: none;
      font-size: 18px;
      text-transform: uppercase;
      padding: 19px 40px;
      transition: opacity 0.5s;
      opacity: 0.7;
      ${media.phone`padding: 19px;`}

      &:active,
      &:visited,
      &:focus,
      &:hover {
        text-decoration: none;
        color: white;
        opacity: 1;
      }
    }
  }
`;

const Content = styled.div`
  width: 80%;
  margin: 0 auto;
  ${media.phone`width: 95%;`}
`;

const Application = () => {
  return (
    <Router>
      <GlobalStyle />
      <SoccerAppWrapper>
        <StyledAppBar position="static" color="default">
          <Toolbar>
            <NavLink to="/">Planning Equipe Soccer 5</NavLink>
            <NavLink to="/agenda">Agenda</NavLink>
            <NavLink to="/players">Players</NavLink>
          </Toolbar>
        </StyledAppBar>
        <Content>
          <Route path="/" exact component={Season} />
          <Route path="/agenda" component={Agenda} />
          <Route path="/players" component={Players} />
        </Content>
      </SoccerAppWrapper>
    </Router>
  );
};

export default Application;
