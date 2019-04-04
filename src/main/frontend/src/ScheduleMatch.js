import React, { Fragment, useState, useEffect } from "react";
import styled from "styled-components";
import Button from "@material-ui/core/Button";

var moment = require("moment");
import axios from "axios";

const Player = styled.div`
  display: grid;
  grid-template-columns: 190px 60px;
  grid-auto-rows: 35px;
  align-items: center;
`;

const CancelBtn = styled.div`
  cursor: pointer;
`;

const MatchWithPlayer = styled.div``;
const MatchToBePlanned = styled.div``;
const NoMatch = styled.div`
  margin-top: 20px;
`;
const PlanButton = styled(Button)`
  && {
    margin-left: 30px;
    margin-bottom: 10px;
  }
`;
const Title = styled.h3`
  font-size: 20px;
`;

const ScheduleMatch = ({ matchType }) => {
  const [scheduledMatches, setScheduledMatches] = useState([]);
  const [matchesList, setMatchesList] = useState([]);
  const [updateToggle, setUpdateToggle] = useState(false);
  const [matchToPlanCount, setMatchToPlanCount] = useState(0);

  useEffect(() => {
    async function fetchData() {
      const result = await axios(
        `/rest/seasons/current/matches/${matchType}/next`
      );
      setScheduledMatches(result.data);
    }

    fetchData();
  }, [updateToggle]);

  useEffect(() => {
    async function fetchData() {
      const result = await axios(
        `rest/seasons/current/matches/${matchType}/to-plan`
      );
      setMatchesList(result.data);
      setMatchToPlanCount(result.data.length);
    }

    fetchData();
  }, [matchToPlanCount]);

  const handleSubstitute = async (date, player) => {
    await axios.delete(
      `/rest/seasons/current/matches/${matchType}/${date}/players/${player}`
    );
    setUpdateToggle(!updateToggle);
  };

  const planHanlder = async date => {
    await axios.put(`/rest/seasons/current/matches/${matchType}/${date}`);
    setUpdateToggle(!updateToggle);
    setMatchToPlanCount(matchToPlanCount - 1);
  };

  const intersperse = (arr, sep) => {
    if (arr.length === 0) {
      return [];
    }
    return arr.slice(1).reduce((xs, x, i) => xs.concat([sep, x]), [arr[0]]);
  };

  return (
    <Fragment>
      {scheduledMatches.length > 0 && (
        <MatchWithPlayer>
          <Title>Next {matchType} matches</Title>
          {scheduledMatches.map(match => (
            <div key={`match-${match.date}`}>
              <h4>{moment(match.date).format("dddd YYYY/MM/DD")}</h4>
              {match.players.map(player => (
                <Player key={`player-${player}`}>
                  <span>{player}</span>
                  <span>
                    {match.subs.length !== 0 && match.hasMinimumPlayer && (
                      <CancelBtn
                        onClick={() => handleSubstitute(match.date, player)}
                      >
                        ❎
                      </CancelBtn>
                    )}
                  </span>
                </Player>
              ))}
              <i>Substitutes : </i>{" "}
              {match.subs.length == 0 ? "None" : intersperse(match.subs, ", ")}
            </div>
          ))}
        </MatchWithPlayer>
      )}
      {matchesList.length > 0 && (
        <MatchToBePlanned>
          <Title>Matches to plan</Title>
          {matchesList.map(match => (
            <div key={match.date}>
              {match.date}
              <PlanButton
                color="primary"
                size="small"
                variant="contained"
                onClick={() => planHanlder(match.date)}
              >
                PLAN
              </PlanButton>
            </div>
          ))}
        </MatchToBePlanned>
      )}
      {scheduledMatches.length === 0 && matchesList.length === 0 && (
        <NoMatch>No match to plan</NoMatch>
      )}
    </Fragment>
  );
};

export default ScheduleMatch;
