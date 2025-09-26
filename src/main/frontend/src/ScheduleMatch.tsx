import React, { Fragment } from "react";
import styled from "styled-components";
import {
  Button,
  Card,
  CardContent,
  Typography,
  Box,
  Chip,
  IconButton,
  Avatar,
  Divider,
  Stack,
  Paper,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  ListItemSecondaryAction,
  Alert
} from "@mui/material";
import {
  PersonRemove,
  CalendarMonth,
  SportsSoccer,
  People,
  PersonAdd,
  Schedule,
  Share
} from "@mui/icons-material";

import moment from "moment";
import { Match, MatchToPlan } from "./types";
import { useMatches, useMatchesToPlan, useSubstitutePlayer, usePlanMatch } from "./hooks/useQueries";

interface ScheduleMatchProps {
  matchType: 'friendly' | 'league';
}

const MatchesContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  gap: 24px;
`;

const MatchCard = styled(Card)`
  && {
    border-radius: 16px;
    box-shadow: 0 8px 32px rgba(255, 102, 0, 0.15);
    border: 1px solid ${props => props.theme.palette.primary.light}20;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    
    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 16px 48px rgba(255, 102, 0, 0.2);
    }
  }
`;

const SectionTitle = styled(Typography)`
  && {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    font-weight: 600;
    color: ${props => props.theme.palette.primary.main};
  }
`;

const DateChip = styled(Chip)`
  && {
    background: linear-gradient(135deg, #ff6600 0%, #ff8533 100%);
    color: white;
    font-weight: 600;
    margin-bottom: 16px;
    font-size: 14px;
  }
`;

const PlayersGrid = styled(Box)`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
  margin-top: 16px;
`;

const PlayerCard = styled(Paper)`
  && {
    padding: 12px;
    border-radius: 12px;
    background: ${props => props.theme.palette.background.paper};
    border: 1px solid ${props => props.theme.palette.divider};
    transition: all 0.2s ease;
    
    &:hover {
      background: ${props => props.theme.palette.action.hover};
      transform: translateY(-1px);
    }
  }
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

  const generateWhatsAppMessage = (match: Match) => {
    const formattedDate = new Date(match.date).toLocaleDateString(navigator.language, {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
    const squadList = match.players.join('\n• ');
    const subsText = match.subs.length > 0 ? `\n\n🔄 Substitutes:\n• ${match.subs.join('\n• ')}` : '';
    
    const message = `
📅 Date: ${formattedDate}

👥 List:
• ${squadList}${subsText}

`;

    return message;
  };

  const handleCopyToClipboard = async (match: Match) => {
    const message = generateWhatsAppMessage(match);
    
    try {
      await navigator.clipboard.writeText(message);
    } catch (err) {
      // Fallback for older browsers
      const textArea = document.createElement('textarea');
      textArea.value = message;
      document.body.appendChild(textArea);
      textArea.select();
      document.execCommand('copy');
      document.body.removeChild(textArea);
    }
  };

  const intersperse = (arr: string[], sep: string): (string | string[])[] => {
    if (arr.length === 0) {
      return [];
    }
    return arr.slice(1).reduce((xs, x, i) => xs.concat([sep, x]), [arr[0]]);
  };

  if (matchesLoading || matchesToPlanLoading) {
    return (
      <Box display="flex" alignItems="center" gap={2} justifyContent="center" py={4}>
        <SportsSoccer sx={{ color: 'primary.main', animation: 'spin 1s linear infinite' }} />
        <Typography variant="h6">Loading {matchType} matches...</Typography>
      </Box>
    );
  }

  if (matchesError || matchesToPlanError) {
    return (
      <Alert severity="error" sx={{ borderRadius: 2 }}>
        <Typography variant="h6">⚠️ Error loading {matchType} matches</Typography>
      </Alert>
    );
  }

  return (
    <MatchesContainer>
      {scheduledMatches.length > 0 && (
        <Box>
          <SectionTitle variant="h5">
            <SportsSoccer />
            ⚽ Upcoming {matchType} matches
          </SectionTitle>
          
          <Stack spacing={3}>
            {scheduledMatches.map(match => (
              <MatchCard key={`match-${match.date}`}>
                <CardContent>
                  <Box display="flex" alignItems="center" justifyContent="space-between" mb={2}>
                    <DateChip 
                      icon={<CalendarMonth />}
                      label={moment(match.date).format("dddd, MMMM Do YYYY")}
                      size="medium"
                    />
                    <IconButton
                      onClick={() => handleCopyToClipboard(match)}
                      color="primary"
                      sx={{
                        backgroundColor: 'primary.light',
                        color: 'white',
                        '&:hover': {
                          backgroundColor: 'primary.main',
                          transform: 'scale(1.1)'
                        },
                        transition: 'all 0.2s ease'
                      }}
                      aria-label="Share match details"
                    >
                      <Share />
                    </IconButton>
                  </Box>
                  
                  <Box display="flex" alignItems="center" gap={1} mb={2}>
                    <People color="primary" />
                    <Typography variant="h6" fontWeight={600}>
                      Squad ({match.players.length} players)
                    </Typography>
                  </Box>

                  <PlayersGrid>
                    {match.players.map(player => (
                      <PlayerCard key={`player-${player}`} elevation={1}>
                        <Box display="flex" alignItems="center" justifyContent="space-between">
                          <Box display="flex" alignItems="center" gap={2}>
                            <Avatar sx={{ bgcolor: 'primary.main', width: 32, height: 32 }}>
                              👤
                            </Avatar>
                            <Typography variant="body1" fontWeight={500}>
                              {player}
                            </Typography>
                          </Box>
                          {(match.subs.length !== 0 || (match.subs.length === 0 && !match.hasMinimumPlayer)) && (
                            <IconButton
                              onClick={() => handleSubstitute(match.date, player)}
                              color="error"
                              size="small"
                              sx={{
                                '&:hover': {
                                  backgroundColor: 'error.light',
                                  color: 'white',
                                  transform: 'scale(1.1)'
                                },
                                transition: 'all 0.2s ease'
                              }}
                            >
                              <PersonRemove />
                            </IconButton>
                          )}
                        </Box>
                      </PlayerCard>
                    ))}
                  </PlayersGrid>

                  {match.subs.length > 0 && (
                    <>
                      <Divider sx={{ my: 3 }} />
                      <Box display="flex" alignItems="center" gap={1} mb={2}>
                        <PersonAdd color="secondary" />
                        <Typography variant="h6" fontWeight={600}>
                          🔄 Substitutes
                        </Typography>
                      </Box>
                      <Stack direction="row" spacing={1} flexWrap="wrap">
                        {match.subs.map((sub, index) => (
                          <Chip
                            key={`sub-${index}`}
                            label={sub}
                            variant="outlined"
                            color="secondary"
                            avatar={<Avatar>🔄</Avatar>}
                            sx={{ mb: 1 }}
                          />
                        ))}
                      </Stack>
                    </>
                  )}
                </CardContent>
              </MatchCard>
            ))}
          </Stack>
        </Box>
      )}

      {matchesList.length > 0 && (
        <Box>
          <SectionTitle variant="h5">
            <Schedule />
            📅 Matches to plan
          </SectionTitle>
          
          <Stack spacing={2}>
            {matchesList.map(match => (
              <MatchCard key={match.date}>
                <CardContent>
                  <Box display="flex" alignItems="center" justifyContent="space-between">
                    <Box display="flex" alignItems="center" gap={2}>
                      <CalendarMonth color="primary" />
                      <Typography variant="h6" fontWeight={600}>
                        {moment(match.date).format("dddd, MMMM Do YYYY")}
                      </Typography>
                    </Box>
                    <Button
                      variant="contained"
                      color="primary"
                      onClick={() => planHanlder(match.date)}
                      startIcon={<SportsSoccer />}
                      sx={{
                        borderRadius: 3,
                        px: 3,
                        py: 1,
                        fontWeight: 600,
                        textTransform: 'none',
                        boxShadow: '0 4px 12px rgba(255, 102, 0, 0.3)',
                        '&:hover': {
                          transform: 'translateY(-2px)',
                          boxShadow: '0 8px 20px rgba(255, 102, 0, 0.4)'
                        },
                        transition: 'all 0.2s ease'
                      }}
                    >
                      ⚽ Plan Match
                    </Button>
                  </Box>
                </CardContent>
              </MatchCard>
            ))}
          </Stack>
        </Box>
      )}

      {scheduledMatches.length === 0 && matchesList.length === 0 && (
        <Alert severity="info" sx={{ borderRadius: 2, textAlign: 'center', py: 4 }}>
          <Typography variant="h6" gutterBottom>
            ⚽ No {matchType} matches scheduled
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Check back later for upcoming matches!
          </Typography>
        </Alert>
      )}
    </MatchesContainer>
  );
};

export default ScheduleMatch;
