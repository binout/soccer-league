import React, { Fragment } from "react";
import styled from "styled-components";
import {
  Card,
  CardContent,
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Avatar,
  Chip,
  Alert,
  Stack,
  useMediaQuery
} from "@mui/material";
import {
  BarChart
} from "@mui/icons-material";
import { useTheme } from "@mui/material/styles";
import { StatisticsData } from "./types";
import { useSeasonStats } from "./hooks/useQueries";

const StatisticsContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  gap: 24px;
`;

const StatsCard = styled(Card)`
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
    margin-bottom: 24px;
    font-weight: 600;
    color: ${props => props.theme.palette.primary.main};
  }
`;

const StyledTableContainer = styled(TableContainer)`
  && {
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  }
`;

const Statistics: React.FC = () => {
  const { data: stats = [], isLoading, error } = useSeasonStats();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

  if (isLoading) {
    return (
      <Box display="flex" alignItems="center" gap={2} justifyContent="center" py={4}>
        <BarChart sx={{ color: 'primary.main', animation: 'pulse 1.5s ease-in-out infinite' }} />
        <Typography variant="h6">📊 Loading statistics...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ borderRadius: 2 }}>
        <Typography variant="h6">⚠️ Error loading statistics</Typography>
      </Alert>
    );
  }

  // Calculate most active player(s)
  const maxMatches = Math.max(...stats.map(stat => stat.nbMatches), 0);
  const mostActivePlayers = maxMatches > 0 ? stats.filter(stat => stat.nbMatches === maxMatches) : [];

  return (
    <StatisticsContainer>
      <SectionTitle variant="h5">
        <BarChart />
        Season Statistics
      </SectionTitle>

      {/* Most Active Player(s) Highlight */}
      {mostActivePlayers.length > 0 && (
        <Alert 
          severity="success" 
          sx={{ 
            borderRadius: 2, 
            backgroundColor: '#fff3e0',
            border: '1px solid #ff6600',
            '& .MuiAlert-icon': {
              color: '#ff6600'
            }
          }}
        >
          <Typography variant="h6" gutterBottom>
            🏆 Most Active Player{mostActivePlayers.length > 1 ? 's' : ''}
          </Typography>
          <Typography variant="body1">
            {mostActivePlayers.length === 1 ? (
              <>
                <strong>{mostActivePlayers[0].player}</strong> leads with {mostActivePlayers[0].nbMatches} total matches!
              </>
            ) : (
              <>
                <strong>{mostActivePlayers.map(p => p.player).join(', ')}</strong> are tied with {maxMatches} total matches each!
              </>
            )}
          </Typography>
        </Alert>
      )}

      {/* Detailed Statistics Table */}
      <StatsCard>
        <CardContent>
          <Typography variant="h6" gutterBottom fontWeight={600} sx={{ mb: 3 }}>
            📈 Player Performance Details
          </Typography>
          
          <StyledTableContainer component={Paper}>
            <Table stickyHeader>
              <TableHead>
                <TableRow>
                  <TableCell>
                    <Typography variant="subtitle2" fontWeight={600}>
                      👤 Player
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      ⚽ Friendly
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      🏁 League
                    </Typography>
                  </TableCell>
                  <TableCell align="center">
                    <Typography variant="subtitle2" fontWeight={600}>
                      📊 Total
                    </Typography>
                  </TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {stats
                  .sort((a, b) => b.nbMatches - a.nbMatches) // Sort by total matches descending
                  .map((stat: StatisticsData, index) => (
                  <TableRow 
                    key={stat.player} 
                    hover
                    sx={{
                      '&:hover': {
                        backgroundColor: '#fff3e0'
                      }
                    }}
                  >
                    <TableCell>
                      <Box display="flex" alignItems="center" gap={2}>
                        <Avatar 
                          sx={{ 
                            bgcolor: stat.nbMatches === maxMatches && maxMatches > 0 ? '#ff6600' : 'primary.light',
                            width: 32, 
                            height: 32 
                          }}
                        >
                          {stat.nbMatches === maxMatches && maxMatches > 0 ? '🏆' : '👤'}
                        </Avatar>
                        <Typography variant="body2" fontWeight={500}>
                          {stat.player}
                        </Typography>
                        {stat.nbMatches === maxMatches && maxMatches > 0 && (
                          <Chip 
                            label="Top Player" 
                            size="small" 
                            color="primary"
                            sx={{ ml: 1 }}
                          />
                        )}
                      </Box>
                    </TableCell>
                    <TableCell align="center">
                      <Chip 
                        label={stat.nbFriendlyMatches}
                        variant="outlined"
                        color="primary"
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Chip 
                        label={stat.nbLeagueMatches}
                        variant="outlined"
                        color="secondary"
                        size="small"
                      />
                    </TableCell>
                    <TableCell align="center">
                      <Chip 
                        label={stat.nbMatches}
                        variant="filled"
                        color="primary"
                        size="small"
                        sx={{ fontWeight: 'bold' }}
                      />
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </StyledTableContainer>
        </CardContent>
      </StatsCard>
    </StatisticsContainer>
  );
};

export default Statistics;