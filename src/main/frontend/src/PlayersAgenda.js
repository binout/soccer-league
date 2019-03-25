import React, { Fragment, useEffect, useState } from "react";
import Button from "@material-ui/core/Button";
import styled from "styled-components";
import Checkbox from "@material-ui/core/Checkbox";
import DatePicker from "react-datepicker";
import axios from "axios";

var moment = require("moment");
import "react-datepicker/dist/react-datepicker.css";

const Badge = styled.div`
  width: 20px;
  height: 20px;
  margin-left: 10px;
  font-size: 10px;
  border-radius: 10px;
  background-color: ${props => (props.canBePlanned ? "#2e7d32" : "#ffc107")};
  text-align: center;
  padding-top: 2px;
  color: white;
`;

const DatePickerWrapper = styled.div``;
const PlayersPlanning = styled.div`
  margin-top: 30px;
`;
const PlayerLine = styled.div`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 200px)`
      : `200px 200px`};
  align-items: center;
`;
const PlanningHeader = styled.div`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 200px)`
      : `200px 200px`};
  font-size: 16px;
  font-weight: bold;
`;
const MatchDate = styled.span`
  display: inline-flex;
`;

const PlayersAgenda = ({ matchType }) => {
  const [date, setDate] = useState(new Date());
  const [updateMatchStateToggle, setUpdateMatchStateToggle] = useState(false);
  const [matchDates, setMatchDates] = useState([]);
  const [players, setPlayers] = useState([]);

  useEffect(() => {
    const fetchMatchDates = async () => {
      const result = await axios.get(`/rest/match-dates/${matchType}/next`);
      setMatchDates(result.data);
    };
    fetchMatchDates();
  }, [updateMatchStateToggle]);

  useEffect(() => {
    const fetchPlayers = async () => {
   
      if(matchType==='friendly'){
         const result = await axios.get("/rest/players");
         setPlayers(result.data);
      }else{
        const result = await axios.get("/rest/players/league");
        setPlayers(result.data);
      }
      
    };
    fetchPlayers();
  }, []);

  const handleOnCheck = async (date, player, checked) => {
    if (checked) {
      await axios.put(
        `/rest/match-dates/${matchType}/${date}/players/${player}`
      );
      setUpdateMatchStateToggle(!updateMatchStateToggle);
    } else {
      await axios.delete(
        `/rest/match-dates/${matchType}/${date}/players/${player}`
      );
      setUpdateMatchStateToggle(!updateMatchStateToggle);
    }
  };

  const handleSubmit = async () => {
    const newMatchDate = moment(date).format("YYYY-MM-DD");
    await axios.put(`/rest/match-dates/${matchType}/${newMatchDate}`);
    setUpdateMatchStateToggle(!updateMatchStateToggle);
  };

  return (
    <Fragment>
      <DatePickerWrapper>
        <DatePicker
          selected={date}
          onChange={date => setDate(date)}
          dateFormat="yyyy/MM/dd"
        />
        <Button variant="contained" color="primary" onClick={handleSubmit}>
          ADD
        </Button>
      </DatePickerWrapper>
      {matchDates.length > 0 && (
        <PlayersPlanning>
          <PlanningHeader column={matchDates.length}>
            <span>Players</span>
            {matchDates.map(matchDate => {
              return (
                <MatchDate key={`header-${matchDate.date}`}>
                  {matchDate.date}
                  <Badge canBePlanned={matchDate.canBePlanned}>
                    {matchDate.presents.length}
                  </Badge>
                </MatchDate>
              );
            })}
          </PlanningHeader>
          {players.map(player => {
            return (
              <PlayerLine
                key={`playerLine-${player.name}`}
                column={matchDates.length}
              >
                <span>{player.name}</span>

                {matchDates.map(matchDate => {
                  return (
                    <span key={`checkbox-${matchDate.date}-${player.name}`}>
                      <Checkbox
                        type="checkbox"
                        checked={matchDate.presents.includes(player.name)}
                        onChange={(evt, checked) =>
                          handleOnCheck(matchDate.date, player.name, checked)
                        }
                      />
                    </span>
                  );
                })}
              </PlayerLine>
            );
          })}
        </PlayersPlanning>
      )}
    </Fragment>
  );
};

export default PlayersAgenda;
