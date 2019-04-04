import React, { Fragment, useEffect, useState } from "react";
import Button from "@material-ui/core/Button";
import styled from "styled-components";
import Checkbox from "@material-ui/core/Checkbox";
import axios from "axios";
import { MuiPickersUtilsProvider, InlineDatePicker } from "material-ui-pickers";
import DateFnsUtils from "@date-io/moment";
import { media } from "./style";
var moment = require("moment");

const Badge = styled.div`
  width: 20px;
  height: 15px;
  margin-left: 10px;
  font-size: 10px;
  border-radius: 10px;
  background-color: ${props => (props.canBePlanned ? "#2e7d32" : "#ffc107")};
  text-align: center;
  padding-top: 2px;
  color: white;
`;

const DatePickerWrapper = styled.div`
  margin-left: 15px;
`;
const PlayersPlanning = styled.div`
  margin-top: 30px;
  margin-left: 15px;
  overflow-y: hidden;
`;
const PlayerLine = styled.div`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 200px)`
      : `200px 200px`};
  align-items: center;
  grid-auto-rows: 35px;
  ${media.phone`
    grid-template-columns: ${props => props.column ? `[first] 80px repeat(${props.column}, 1fr)`: `repeat(2, 1fr)`};
    grid-auto-rows: auto;
  `}
`;
const PlanningHeader = styled.div`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 200px)`
      : `200px 200px`};
  font-size: 16px;
  font-weight: bold;
  ${media.phone`
    grid-template-columns: ${props => props.column ? `[first] 80px repeat(${props.column}, 1fr)`: `repeat(2, 1fr)`};
    grid-auto-rows: auto;    
  `}
`;
const MatchDate = styled.span`
  display: inline-flex;
`;
const AddBtn = styled(Button)`
  && {
    margin-top: 10px;
    margin-left: 15px;
  }
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
      if (matchType === "friendly") {
        const result = await axios.get("/rest/players");
        setPlayers(result.data);
      } else {
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
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
          <InlineDatePicker
            variant="outlined"
            onlyCalendar
            label="Match date"
            value={date}
            onChange={date => {
              setDate(date);
            }}
          />
        </MuiPickersUtilsProvider>

        <AddBtn variant="contained" color="primary" onClick={handleSubmit}>
          ADD
        </AddBtn>
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
