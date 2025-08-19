import React, { Fragment } from "react";
import styled from "styled-components";
import Button from "@mui/material/Button";

import moment from "moment";
import { Match, MatchToPlan } from "./types";
import { useMatches, useMatchesToPlan, useSubstitutePlayer, usePlanMatch } from "./hooks/useQueries";

interface ScheduleMatchProps {
  matchType: 'friendly' | 'league';
}

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

const ScheduleMatch: React.FC<ScheduleMatchProps> = ({ matchType }) => {
  // React Query hooks
  const { data: scheduledMatches = [], isLoading: matchesLoading, error: matchesError } = useMatches(matchType);
  const { data: matchesList = [], isLoading: matchesToPlanLoading, error: matchesToPlanError } = useMatchesToPlan(matchType);
  
  const substitutePlayerMutation = useSubstitutePlayer();
  const planMatchMutation = usePlanMatch();

  const handleSubstitute = (date: string, player: string) => {
    substitutePlayerMutation.mutate({
      matchType,
      date,
      playerId: player
    });
  };

  const planHanlder = (date: string) => {
    planMatchMutation.mutate({
      matchType,
      date
    });
  };

  const intersperse = (arr: string[], sep: string): (string | string[])[] => {
    if (arr.length === 0) {
      return [];
    }
    return arr.slice(1).reduce((xs, x, i) => xs.concat([sep, x]), [arr[0]]);
  };

  if (matchesLoading || matchesToPlanLoading) {
    return <div>Loading matches...</div>;
  }

  if (matchesError || matchesToPlanError) {
    return <div>Error loading matches</div>;
  }

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
                    {(match.subs.length !== 0 || (match.subs.length === 0 && !match.hasMinimumPlayer)) && (
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
              {match.subs.length === 0 ? "None" : intersperse(match.subs, ", ")}
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
