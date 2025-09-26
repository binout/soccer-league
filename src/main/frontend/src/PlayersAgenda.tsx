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
  isCanBePlanned: boolean;
}

const Badge = styled.div<BadgeProps>`
  min-width: 28px;
  min-height: 24px;
  margin-left: 8px;
  font-size: 12px;
  border-radius: 14px;
  background-color: ${props => (props.isCanBePlanned ? "#2e7d32" : "#ffc107")};
  text-align: center;
  padding: 6px 8px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  flex-shrink: 0;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.2);
  
  ${media.phone`
    min-width: 20px;
    min-height: 18px;
    font-size: 9px;
    padding: 2px 4px;
    margin-left: 2px;
    border-radius: 8px;
  `}
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
  margin-right: 15px;
  overflow-y: hidden;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  width: calc(100% - 30px);
  
  ${media.phone`
    margin-left: 8px;
    margin-right: 8px;
    width: calc(100% - 16px);
  `}
`;
interface GridProps {
  column?: number;
}

const PlayerLine = styled.div<GridProps>`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 240px)`
      : `200px 240px`};
  align-items: center;
  grid-auto-rows: 50px; /* Increased height for better touch targets */
  min-height: ${responsive.touchTarget};
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  
  & > span:first-child {
    padding-left: ${responsive.spacing.md};
  }
  
  & > span:not(:first-child) {
    display: flex;
    align-items: center;
    justify-content: center;
    padding-left: ${responsive.spacing.md};
    padding-right: ${responsive.spacing.md};
  }
  
  ${media.phone`
    grid-template-columns: ${(props: GridProps) => props.column ? `[first] 120px repeat(${props.column}, minmax(80px, 1fr))`: `120px minmax(80px, 1fr)`};
    grid-auto-rows: auto;
    min-height: ${responsive.touchTarget};
    padding: ${responsive.spacing.sm} 0;
    
    & > span:first-child {
      padding-left: ${responsive.spacing.sm};
    }
    
    & > span:not(:first-child) {
      padding-left: ${responsive.spacing.xs};
      padding-right: ${responsive.spacing.xs};
    }
  `}
  
  &:hover {
    background-color: rgba(0, 0, 0, 0.02);
  }
`;
const PlanningHeader = styled.div<GridProps>`
  display: grid;
  grid-template-columns: ${props =>
    props.column
      ? `[first] 200px repeat(${props.column}, 240px)`
      : `200px 240px`};
  font-size: 16px;
  font-weight: bold;
  align-items: center;
  min-height: ${responsive.touchTarget};
  background-color: rgba(0, 0, 0, 0.05);
  padding: ${responsive.spacing.sm} 0;
  border-radius: 4px 4px 0 0;
  margin-bottom: ${responsive.spacing.xs};
  width: 100%;
  min-width: max-content;
  
  & > span:first-child {
    padding-left: ${responsive.spacing.md};
  }
  
  & > span:not(:first-child) {
    padding-left: ${responsive.spacing.md};
    padding-right: ${responsive.spacing.md};
  }
  
  ${media.phone`
    grid-template-columns: ${(props: GridProps) => props.column ? `[first] 120px repeat(${props.column}, minmax(80px, 1fr))`: `120px minmax(80px, 1fr)`};
    grid-auto-rows: auto;
    font-size: ${responsive.fontSize.sm};
    padding: ${responsive.spacing.md} 0;
    
    & > span:first-child {
      padding-left: ${responsive.spacing.sm};
    }
    
    & > span:not(:first-child) {
      padding-left: ${responsive.spacing.xs};
      padding-right: ${responsive.spacing.xs};
    }
  `}
`;
const MatchDate = styled.span`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: nowrap;
  gap: 4px;
  min-width: 0;
  width: 100%;
  
  ${media.phone`
    flex-direction: column;
    align-items: center;
    gap: 2px;
    text-align: center;
    justify-content: center;
  `}
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
                  <Badge isCanBePlanned={matchDate.isCanBePlanned}>
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
