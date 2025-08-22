import React, { Fragment, useState } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import styled from "styled-components";
import Checkbox from "@mui/material/Checkbox";
// Using HTML5 date input instead of deprecated @mui/lab DatePicker
import { media, responsive } from "./style";
import moment from "moment";
import { Player, type MatchDate } from "./types";
import { useMatchDates, usePlayers, useLeaguePlayers, useCreateMatchDate, usePlayerPresence } from "./hooks/useQueries";

interface PlayersAgendaProps {
  matchType: 'friendly' | 'league';
}

interface BadgeProps {
  canBePlanned: boolean;
}

const Badge = styled.div<BadgeProps>`
  min-width: 24px;
  min-height: 20px;
  margin-left: 10px;
  font-size: 11px;
  border-radius: 12px;
  background-color: ${props => (props.canBePlanned ? "#2e7d32" : "#ffc107")};
  text-align: center;
  padding: 4px 6px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
`;

const DatePickerWrapper = styled.div`
  margin-left: 15px;
  display: flex;
  align-items: center;
  gap: ${responsive.spacing.md};
  flex-wrap: wrap;
  
  ${media.sm`
    flex-wrap: nowrap;
  `}
`;

const PlayersPlanning = styled.div`
  margin-top: 30px;
  margin-left: 15px;
  overflow-y: hidden;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
`;
interface GridProps {
  column?: number;
}

const PlayerLine = styled.div<GridProps>`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 200px)`
      : `200px 200px`};
  align-items: center;
  grid-auto-rows: 50px; /* Increased height for better touch targets */
  min-height: ${responsive.touchTarget};
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  
  ${media.phone`
    grid-template-columns: ${(props: GridProps) => props.column ? `[first] 120px repeat(${props.column}, 1fr)`: `repeat(2, 1fr)`};
    grid-auto-rows: auto;
    min-height: ${responsive.touchTarget};
    padding: ${responsive.spacing.sm} 0;
  `}
  
  &:hover {
    background-color: rgba(0, 0, 0, 0.02);
  }
`;
const PlanningHeader = styled.div<GridProps>`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 200px)`
      : `200px 200px`};
  font-size: 16px;
  font-weight: bold;
  align-items: center;
  min-height: ${responsive.touchTarget};
  background-color: rgba(0, 0, 0, 0.05);
  padding: ${responsive.spacing.sm} 0;
  border-radius: 4px 4px 0 0;
  margin-bottom: ${responsive.spacing.xs};
  
  ${media.phone`
    grid-template-columns: ${(props: GridProps) => props.column ? `[first] 120px repeat(${props.column}, 1fr)`: `repeat(2, 1fr)`};
    grid-auto-rows: auto;
    font-size: ${responsive.fontSize.sm};
    padding: ${responsive.spacing.md} 0;
  `}
`;
const MatchDate = styled.span`
  display: inline-flex;
`;
const AddBtn = styled(Button)`
  && {
    margin-top: ${responsive.spacing.sm};
    min-height: ${responsive.touchTarget};
    min-width: ${responsive.touchTarget};
    padding: ${responsive.spacing.md} ${responsive.spacing.lg};
    font-weight: 600;
    font-size: ${responsive.fontSize.md};
    
    ${media.sm`
      margin-top: 0;
    `}
  }
`;

const PlayersAgenda: React.FC<PlayersAgendaProps> = ({ matchType }) => {
  const [date, setDate] = useState<Date>(new Date());

  // React Query hooks
  const { data: matchDates = [], isLoading: matchDatesLoading, error: matchDatesError } = useMatchDates(matchType);
  const { data: allPlayers = [], isLoading: allPlayersLoading, error: allPlayersError } = usePlayers();
  const { data: leaguePlayers = [], isLoading: leaguePlayersLoading, error: leaguePlayersError } = useLeaguePlayers();
  
  const createMatchDateMutation = useCreateMatchDate();
  const playerPresenceMutations = usePlayerPresence();

  // Select the right players based on match type
  const players = matchType === 'friendly' ? allPlayers : leaguePlayers;
  const playersLoading = matchType === 'friendly' ? allPlayersLoading : leaguePlayersLoading;
  const playersError = matchType === 'friendly' ? allPlayersError : leaguePlayersError;

  const handleOnCheck = async (date: string, player: string, checked: boolean) => {
    if (checked) {
      playerPresenceMutations.add.mutate({
        matchType,
        date,
        playerId: player
      });
    } else {
      playerPresenceMutations.remove.mutate({
        matchType,
        date,
        playerId: player
      });
    }
  };

  const handleSubmit = () => {
    const newMatchDate = moment(date).format("YYYY-MM-DD");
    createMatchDateMutation.mutate({
      matchType,
      date: newMatchDate
    });
  };

  if (matchDatesLoading || playersLoading) {
    return <div>Loading...</div>;
  }

  if (matchDatesError || playersError) {
    return <div>Error loading data</div>;
  }

  return (
    <Fragment>
      <DatePickerWrapper>
        <TextField
          label="Match date"
          type="date"
          value={date ? moment(date).format("YYYY-MM-DD") : ""}
          onChange={(event: React.ChangeEvent<HTMLInputElement>) => {
            const newDate = event.target.value ? moment(event.target.value).toDate() : new Date();
            setDate(newDate);
          }}
          InputLabelProps={{
            shrink: true,
          }}
        />

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
                        checked={matchDate.presents.includes(player.name)}
                        onChange={(evt, checked) =>
                          handleOnCheck(matchDate.date, player.name, checked)
                        }
                        sx={{
                          padding: '1rem',
                          '&:hover': {
                            backgroundColor: 'rgba(0, 0, 0, 0.04)',
                          },
                          '& .MuiSvgIcon-root': {
                            fontSize: '1.5rem',
                          },
                        }}
                        inputProps={{
                          'aria-label': `Mark ${player.name} present for ${matchDate.date}`
                        }}
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
