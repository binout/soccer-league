import React, { Fragment, useState, useEffect } from "react";
import styled from 'styled-components';
var moment = require("moment");
import axios from 'axios';

const Player = styled.div`
display: grid;
grid-template-columns: 190px 60px;
grid-auto-rows: 35px;
align-items: center;
`;

const CancelBtn = styled.div`
cursor: pointer;
`;

const ScheduledMatchWithPlayer = props => {
  
  const [matches, setMatches] = useState([]); 
  const [shouldUpdate, setShouldUpdate] = useState(false); 

  useEffect(() => {
    async function fetchData() {
      console.log('fetch next match' + shouldUpdate)
      const result = await axios('/rest/seasons/current/matches/friendly/next');
      setMatches(result.data);
    }

    fetchData();
  }, [shouldUpdate]);

  const handleSubstitute = (date, player) => {
    axios.delete(`/rest/seasons/current/matches/friendly/${date}/players/${player}`);

    setShouldUpdate(!shouldUpdate);
    console.log(shouldUpdate)
  }

// TODO :
// Utiliser ce component pour les leagues matches et friendly matches
// sortir statistics en tant que component pour gérer les fetches



  // async handleFriendlySubstitute(date, player) {
  //   const url = `/rest/seasons/current/matches/friendly/${date}/players/${player}`;
  //   const params = {
  //     method: "DELETE",
  //     contentType: "application/json",
  //     data: {}
  //   };
  //   const response = await fetch(url, params);
  //   if (response.ok) {
  //     this.fetchFriendlyMatchStates();
  //     this.fetchStats();
  //   }
  // }


  const intersperse = (arr, sep) => {
    if (arr.length === 0) {
      return [];
    }
    return arr.slice(1).reduce((xs, x, i) => xs.concat([sep, x]), [arr[0]]);
  };

  return (
    <Fragment>
      {matches.map(
        match => (
          <div key={`match-${match.date}`}>
            <h4>{moment(match.date).format("dddd YYYY/MM/DD")}</h4>
            
              {match.players.map(player => (
                <Player key={`player-${player}`}>
                  <span>{player}</span>
                  <span>
                    {match.subs.length !== 0 && (
                      <CancelBtn onClick={() => handleSubstitute(match.date, player)}>❎</CancelBtn>
                    )}
                  </span>
                </Player>
              ))}
            <i>Substitutes : </i>{" "}
            {match.subs.length == 0 ? "None" : intersperse(match.subs, ", ")}
          </div>
        )
      )}
    </Fragment>
  );
};

export default ScheduledMatchWithPlayer;
