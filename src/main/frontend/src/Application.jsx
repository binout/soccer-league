import React from "react";
import { BrowserRouter as Router, Routes, Route, NavLink } from "react-router-dom";
import styled from "styled-components";
import { createGlobalStyle } from "styled-components";
import AppBar from "@mui/material/AppBar";
import { green } from "@mui/material/colors";
import Players from "./Players.jsx";
import Agenda from "./Agenda.jsx";
import Season from "./Season.jsx";
import { Toolbar } from "@mui/material";
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
          <Routes>
            <Route path="/" element={<Season />} />
            <Route path="/agenda" element={<Agenda />} />
            <Route path="/players" element={<Players />} />
          </Routes>
        </Content>
      </SoccerAppWrapper>
    </Router>
  );
};

export default Application;
