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
    border-radius: 12px;
    overflow: hidden;
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
          <CircularProgress size={24} />
          <Typography variant="h4" component="h1">
            Loading players...
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
        <Typography variant="h4" component="h1" fontWeight={600}>
          {players.length} Players
        </Typography>
        <Typography variant="h5" color="text.secondary">
          {nbLeaguePlayers} League Players
        </Typography>
        
        <StatsChips>
          <Chip 
            label="⭐ League Player" 
            variant="outlined" 
            size="small"
          />
          <Chip 
            label="🥅 Goalkeeper" 
            variant="outlined" 
            size="small"
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
                <TableCell>
                  <Typography variant="subtitle2" fontWeight={600}>
                    Email
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
                          />
                        )}
                        {player.isGoalkeeper && (
                          <Chip 
                            label="🥅" 
                            size="small" 
                            color="secondary"
                            variant="filled"
                          />
                        )}
                      </PlayerNameCell>
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2" color="text.secondary">
                        {player.email}
                      </Typography>
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
