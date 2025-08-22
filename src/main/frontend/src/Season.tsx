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
    border-radius: 12px;
    overflow: hidden;
    box-shadow: ${props => props.theme.shadows[2]};
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
          <CircularProgress size={24} />
          <Typography variant="h4" component="h1">
            Loading season...
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
      <Typography variant="h4" component="h1" fontWeight={600}>
        Season {season.name}
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
            <Tab label="Friendly" />
            <Tab label="League" />
            <Tab label="Statistics" />
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
