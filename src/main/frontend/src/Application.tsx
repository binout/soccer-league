import React from "react";
import { BrowserRouter as Router, Routes, Route, NavLink as RouterNavLink } from "react-router-dom";
import { QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import styled from "styled-components";
import { createGlobalStyle } from "styled-components";
import AppBar from "@mui/material/AppBar";
import { green } from "@mui/material/colors";
import Players from "./Players.tsx";
import Agenda from "./Agenda.tsx";
import Season from "./Season.tsx";
import { Toolbar } from "@mui/material";
import { media, responsive } from "./style";
import { queryClient } from "./config/queryClient";

const GlobalStyle = createGlobalStyle`
  * {
    box-sizing: border-box;
  }

  body {
    margin: 0;
    font-family: "Roboto", "Helvetica", "Arial", sans-serif;
    line-height: 1.5;
    
    /* Mobile-first typography */
    font-size: ${responsive.fontSize.md};
    
    ${media.md`
      font-size: ${responsive.fontSize.lg};
    `}
  }

  /* Ensure touch targets are at least 44px */
  button, 
  a,
  input[type="button"],
  input[type="submit"] {
    min-height: ${responsive.touchTarget};
    min-width: ${responsive.touchTarget};
  }
`;

const SoccerAppWrapper = styled.div`
  margin: 0 auto;
`;

const StyledAppBar = styled(AppBar)`
  && {
    background-color: ${green[900]};
    margin-bottom: ${responsive.spacing.lg};
    
    ${media.md`
      margin-bottom: ${responsive.spacing.xxl};
    `}
  }
`;

const StyledToolbar = styled(Toolbar)`
  && {
    flex-direction: column;
    padding: ${responsive.spacing.sm};
    
    ${media.sm`
      flex-direction: row;
      padding: ${responsive.spacing.md};
    `}
  }
`;

const StyledNavLink = styled(RouterNavLink)`
  color: white;
  text-decoration: none;
  text-transform: uppercase;
  font-weight: 500;
  transition: opacity 0.3s;
  opacity: 0.8;
  
  /* Mobile-first: stack vertically with full width touch targets */
  display: block;
  width: 100%;
  text-align: center;
  padding: ${responsive.spacing.md};
  font-size: ${responsive.fontSize.sm};
  min-height: ${responsive.touchTarget};
  
  /* Small screens and up: horizontal layout */
  ${media.sm`
    display: inline-block;
    width: auto;
    padding: ${responsive.spacing.lg} ${responsive.spacing.xl};
    font-size: ${responsive.fontSize.lg};
  `}

  &:active,
  &:visited,
  &:focus,
  &:hover {
    text-decoration: none;
    color: white;
    opacity: 1;
  }

  &.active {
    opacity: 1;
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 4px;
  }
`;

const Content = styled.div`
  /* Mobile-first: full width with padding */
  width: 100%;
  padding: 0 ${responsive.spacing.md};
  margin: 0 auto;
  
  /* Small screens: add more padding */
  ${media.sm`
    padding: 0 ${responsive.spacing.lg};
  `}
  
  /* Medium screens and up: constrain width */
  ${media.md`
    width: 90%;
    padding: 0 ${responsive.spacing.xl};
  `}
  
  /* Large screens: further constrain width */
  ${media.lg`
    width: 85%;
    max-width: 1200px;
  `}
  
  /* Extra large screens: maximum constraint */
  ${media.xl`
    width: 80%;
    max-width: 1400px;
  `}
`;

const Application: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <GlobalStyle />
        <SoccerAppWrapper>
          <StyledAppBar position="static" color="default">
            <StyledToolbar>
              <StyledNavLink to="/">Planning Equipe Soccer 5</StyledNavLink>
              <StyledNavLink to="/agenda">Agenda</StyledNavLink>
              <StyledNavLink to="/players">Players</StyledNavLink>
            </StyledToolbar>
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
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
};

export default Application;
