import React from "react";
import { BrowserRouter as Router, Route } from "react-router";

// import SoccerNavBar from "./SoccerNavBar.jsx";
import Players from "./Players.jsx";
import Agenda from "./Agenda.jsx";
// import Season from "./Season.jsx";

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

const Application = () => (
  <div className="test">
    <Players />
  </div>
);

export default Application;
