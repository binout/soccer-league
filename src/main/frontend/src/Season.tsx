import React, { useState, Fragment } from "react";
import styled, { useTheme } from "styled-components";
import { 
  Tabs, 
  Tab, 
  AppBar, 
  Typography, 
  Paper, 
  Box,
  CircularProgress,
  Alert
} from "@mui/material";
import { useTheme as useMuiTheme } from "@mui/material/styles";
import ScheduleMatch from "./ScheduleMatch.tsx";
import Statistics from "./Statistics.tsx";
import { Season as SeasonType } from "./types";
import { useCurrentSeason } from "./hooks/useQueries";

const SeasonContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  gap: 24px;
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

const TabsContentWrapper = styled(Box)`
  padding: 24px;
`;

const Season: React.FC = () => {
  const [selectedTabValue, setSelectedTabValue] = useState<number>(0);
  const { data: season, isLoading, error } = useCurrentSeason();
  const muiTheme = useMuiTheme();

  const handleChange = (_evt: React.SyntheticEvent, value: number) => {
    setSelectedTabValue(value);
  };

  if (isLoading) {
    return (
      <SeasonContainer>
        <Box display="flex" alignItems="center" gap={2}>
          <CircularProgress size={32} sx={{ color: 'primary.main' }} />
          <Typography variant="h4" component="h1">
            ⚽ Loading season...
          </Typography>
        </Box>
      </SeasonContainer>
    );
  }

  if (error || !season) {
    return (
      <SeasonContainer>
        <Alert severity="error">
          <Typography variant="h6">Error loading season</Typography>
        </Alert>
      </SeasonContainer>
    );
  }

  return (
    <SeasonContainer>
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
        🏆 Season {season.name}
      </Typography>
      
      <StyledPaper elevation={0}>
        <AppBar position="static" color="default" elevation={0}>
          <Tabs 
            value={selectedTabValue} 
            onChange={handleChange}
            variant="fullWidth"
            indicatorColor="primary"
            textColor="primary"
          >
            <Tab label="⚽ Friendly" />
            <Tab label="🏁 League" />
            <Tab label="📊 Statistics" />
          </Tabs>
        </AppBar>
        <TabsContentWrapper>
          {selectedTabValue === 0 && <ScheduleMatch matchType="friendly" />}
          {selectedTabValue === 1 && <ScheduleMatch matchType="league" />}
          {selectedTabValue === 2 && <Statistics />}
        </TabsContentWrapper>
      </StyledPaper>
    </SeasonContainer>
  );
};

export default Season;
