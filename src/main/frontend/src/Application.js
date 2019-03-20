import React from "react";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import styled from "styled-components";
import Paper from "@material-ui/core/Paper";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";

// import SoccerNavBar from "./SoccerNavBar.jsx";
import Players from "./Players.js";
import Agenda from "./Agenda.js";
import Season from "./Season.js";

// const Container = () => {
//   const content =
//     this.props.children == null ? <Season /> : this.props.children;
//   return (
//     <div className="App container">
//       <SoccerNavBar />
//       {content}
//     </div>
//   );
// };
const SoccerAppWrapper = styled(Paper)`
  width: 70%;
  margin: 0 auto;
  padding: 20px;
  header {
    a + a {
      margin-left: 50px;
    }
  }
`;

const Application = () => {
  return (
    <Router>
      <SoccerAppWrapper>
        <AppBar position="static" color="default">
          <Toolbar>
            <Link to="/">Planning Equipe Soccer 5</Link>
            <Link to="/agenda">Agenda</Link>
            <Link to="/players">Players</Link>
          </Toolbar>
        </AppBar>

        <Route path="/" exact component={Season} />
        <Route path="/agenda" component={Agenda} />
        <Route path="/players" component={Players} />
      </SoccerAppWrapper>
    </Router>
  );
};

export default Application;
