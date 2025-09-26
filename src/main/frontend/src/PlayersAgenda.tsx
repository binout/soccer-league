import React, { Fragment, useState } from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import styled from "styled-components";
import Checkbox from "@mui/material/Checkbox";
import {
  Card,
  CardContent,
  Typography,
  Box,
  Chip,
  Avatar,
  Paper,
  Stack
} from "@mui/material";
import {
  CalendarMonth,
  People,
  PersonAdd
} from "@mui/icons-material";
// Using HTML5 date input instead of deprecated @mui/lab DatePicker
import { media, responsive } from "./style";
import moment from "moment";
import { Player, type MatchDate } from "./types";
import { useMatchDates, usePlayers, useLeaguePlayers, useCreateMatchDate, usePlayerPresence } from "./hooks/useQueries";

interface PlayersAgendaProps {
  matchType: 'friendly' | 'league';
}


const DatePickerWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: ${responsive.spacing.md};
  flex-wrap: wrap;
  padding: ${responsive.spacing.md} 0;

  ${media.sm`
    flex-wrap: nowrap;
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
        <Box mt={3}>
          <Typography variant="h6" component="h2" gutterBottom sx={{
            display: 'flex',
            alignItems: 'center',
            gap: 1,
            color: 'primary.main',
            fontWeight: 600,
            mb: 3
          }}>
            <People />
            📅 Match Planning
          </Typography>

          <Box sx={{
            overflowX: 'auto',
            borderRadius: 2,
            border: '1px solid',
            borderColor: 'divider'
          }}>
            <Paper sx={{
              borderRadius: 2,
              overflow: 'hidden',
              minWidth: 'max-content'
            }}>
            {/* Header */}
            <Box
              display="grid"
              gridTemplateColumns={`280px repeat(${matchDates.length}, 200px)`}
              sx={{
                backgroundColor: 'grey.50',
                borderBottom: '2px solid',
                borderColor: 'primary.main',
                minWidth: 'max-content'
              }}
            >
              <Box p={2} display="flex" alignItems="center" gap={1}>
                <People color="primary" />
                <Typography variant="subtitle1" fontWeight={600}>
                  Players
                </Typography>
              </Box>
              {matchDates.map(matchDate => (
                <Box
                  key={`header-${matchDate.date}`}
                  p={2}
                  display="flex"
                  flexDirection="column"
                  alignItems="center"
                  gap={1}
                  borderLeft="1px solid"
                  borderColor="divider"
                >
                  <Chip
                    icon={<CalendarMonth />}
                    label={moment(matchDate.date).format("YYYY-MM-DD")}
                    color="primary"
                    size="small"
                    sx={{ fontWeight: 600 }}
                  />
                  <Chip
                    label={`${matchDate.presents.length} players`}
                    color={matchDate.isCanBePlanned ? "success" : "warning"}
                    variant="outlined"
                    size="small"
                  />
                </Box>
              ))}
            </Box>

            {/* Player rows */}
            <Box sx={{ maxHeight: '60vh', overflowY: 'auto' }}>
              {players.map((player, index) => (
                <Box
                  key={`player-${player.name}`}
                  display="grid"
                  gridTemplateColumns={`280px repeat(${matchDates.length}, 200px)`}
                  sx={{
                    minWidth: 'max-content',
                    borderBottom: index < players.length - 1 ? '1px solid' : 'none',
                    borderColor: 'divider',
                    '&:hover': {
                      backgroundColor: 'action.hover'
                    },
                    transition: 'background-color 0.2s ease'
                  }}
                >
                  {/* Player name column */}
                  <Box p={2} display="flex" alignItems="center" gap={2}>
                    <Avatar sx={{
                      bgcolor: 'primary.main',
                      width: 32,
                      height: 32,
                      fontSize: '0.875rem'
                    }}>
                      👤
                    </Avatar>
                    <Typography variant="body2" fontWeight={500}>
                      {player.name}
                    </Typography>
                  </Box>

                  {/* Checkbox columns for each match date */}
                  {matchDates.map(matchDate => (
                    <Box
                      key={`checkbox-${matchDate.date}-${player.name}`}
                      display="flex"
                      alignItems="center"
                      justifyContent="center"
                      borderLeft="1px solid"
                      borderColor="divider"
                      p={1}
                    >
                      <Checkbox
                        checked={matchDate.presents.includes(player.name)}
                        onChange={(evt, checked) =>
                          handleOnCheck(matchDate.date, player.name, checked)
                        }
                        color="primary"
                        sx={{
                          '&:hover': {
                            backgroundColor: 'rgba(255, 102, 0, 0.04)',
                          },
                        }}
                        inputProps={{
                          'aria-label': `Mark ${player.name} present for ${matchDate.date}`
                        }}
                      />
                    </Box>
                  ))}
                </Box>
              ))}
            </Box>
            </Paper>
          </Box>
        </Box>
      )}
    </Fragment>
  );
};

export default PlayersAgenda;
