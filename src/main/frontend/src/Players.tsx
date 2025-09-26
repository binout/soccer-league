import React from "react";
import styled from "styled-components";
import { 
  Typography, 
  Paper, 
  Box, 
  Chip,
  CircularProgress,
  Alert,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  useMediaQuery
} from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { media, responsive } from "./style";
import { PlayerStats } from "./types";
import { usePlayersStats } from "./hooks/useQueries";

const PlayersContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  gap: 24px;
`;

const HeaderBox = styled(Box)`
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

const StatsChips = styled(Box)`
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 16px;
`;

const StyledPaper = styled(Paper)`
  && {
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 8px 32px rgba(255, 102, 0, 0.15);
    background: linear-gradient(135deg, ${props => props.theme.palette.background.paper} 0%, ${props => props.theme.palette.background.default} 100%);
    border: 1px solid ${props => props.theme.palette.primary.light}20;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 12px 40px rgba(255, 102, 0, 0.2);
    }
  }
`;

const PlayerNameCell = styled(Box)`
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
`;

const Players: React.FC = () => {
  const { data: players = [], isLoading, error } = usePlayersStats();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));
  const isTablet = useMediaQuery(theme.breakpoints.down('md'));

  const nbLeaguePlayers = players.filter((p: PlayerStats) => p.isPlayerLeague).length;

  if (isLoading) {
    return (
      <PlayersContainer>
        <Box display="flex" alignItems="center" gap={2}>
          <CircularProgress size={32} sx={{ color: 'primary.main' }} />
          <Typography variant="h4" component="h1">
            👥 Loading players...
          </Typography>
        </Box>
      </PlayersContainer>
    );
  }

  if (error) {
    return (
      <PlayersContainer>
        <Alert severity="error">
          <Typography variant="h6">Error loading players</Typography>
        </Alert>
      </PlayersContainer>
    );
  }

  return (
    <PlayersContainer>
      <HeaderBox>
        <Typography 
          variant="h4" 
          component="h1" 
          fontWeight={600}
          sx={{
            background: 'linear-gradient(135deg, #ff6600 0%, #ff8533 100%)',
            backgroundClip: 'text',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
            textAlign: 'center',
            mb: 1
          }}
        >
          👥 {players.length} Players
        </Typography>
        <Typography variant="h5" color="text.secondary">
          ⭐ {nbLeaguePlayers} League Players
        </Typography>
        
        <StatsChips>
          <Chip 
            label="⭐ League Player" 
            variant="outlined" 
            size="small"
            sx={{ 
              borderColor: 'primary.main',
              color: 'primary.main',
              '&:hover': { 
                backgroundColor: 'primary.light',
                color: 'white',
                transform: 'scale(1.05)'
              },
              transition: 'all 0.2s ease'
            }}
          />
          <Chip 
            label="🥅 Goalkeeper" 
            variant="outlined" 
            size="small"
            sx={{ 
              borderColor: 'secondary.main',
              color: 'secondary.main',
              '&:hover': { 
                backgroundColor: 'secondary.light',
                color: 'white',
                transform: 'scale(1.05)'
              },
              transition: 'all 0.2s ease'
            }}
          />
        </StatsChips>
      </HeaderBox>

      <StyledPaper elevation={2}>
        <TableContainer>
          <Table stickyHeader>
            <TableHead>
              <TableRow>
                <TableCell>
                  <Typography variant="subtitle2" fontWeight={600}>
                    Name
                  </Typography>
                </TableCell>
                {!isMobile && (
                  <TableCell align="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      Seasons
                    </Typography>
                  </TableCell>
                )}
                {!isTablet && (
                  <TableCell align="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      Matches
                    </Typography>
                  </TableCell>
                )}
              </TableRow>
            </TableHead>
            <TableBody>
              {players
                .sort((p1: PlayerStats, p2: PlayerStats) => p2.nbMatches - p1.nbMatches)
                .map((player: PlayerStats) => (
                  <TableRow key={player.name} hover>
                    <TableCell>
                      <PlayerNameCell>
                        <Typography variant="body2" fontWeight={500}>
                          {player.name}
                        </Typography>
                        {player.isPlayerLeague && (
                          <Chip 
                            label="⭐" 
                            size="small" 
                            color="primary"
                            variant="filled"
                            sx={{
                              fontWeight: 'bold',
                              boxShadow: '0 2px 8px rgba(255, 102, 0, 0.3)',
                              '&:hover': {
                                transform: 'scale(1.1)',
                                boxShadow: '0 4px 12px rgba(255, 102, 0, 0.4)'
                              },
                              transition: 'all 0.2s ease'
                            }}
                          />
                        )}
                        {player.isGoalkeeper && (
                          <Chip 
                            label="🥅" 
                            size="small" 
                            color="secondary"
                            variant="filled"
                            sx={{
                              fontWeight: 'bold',
                              boxShadow: '0 2px 8px rgba(26, 26, 26, 0.3)',
                              '&:hover': {
                                transform: 'scale(1.1)',
                                boxShadow: '0 4px 12px rgba(26, 26, 26, 0.4)'
                              },
                              transition: 'all 0.2s ease'
                            }}
                          />
                        )}
                      </PlayerNameCell>
                    </TableCell>
                    {!isMobile && (
                      <TableCell align="center">
                        <Typography variant="body2">
                          {player.nbSeasons}
                        </Typography>
                      </TableCell>
                    )}
                    {!isTablet && (
                      <TableCell align="center">
                        <Typography variant="body2" fontWeight={500}>
                          {player.nbMatches}
                        </Typography>
                      </TableCell>
                    )}
                  </TableRow>
                ))}
            </TableBody>
          </Table>
        </TableContainer>
      </StyledPaper>
    </PlayersContainer>
  );
};

export default Players;
