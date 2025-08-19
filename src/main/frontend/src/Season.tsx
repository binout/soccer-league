import React, { useState, Fragment } from "react";
import styled from "styled-components";

import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import AppBar from "@mui/material/AppBar";
import { grey } from "@mui/material/colors";
import ScheduleMatch from "./ScheduleMatch.tsx";
import Statistics from "./Statistics.tsx";
import { Season as SeasonType } from "./types";
import { useCurrentSeason } from "./hooks/useQueries";

const TabsContentWrapper = styled.div`
  display: flex;
  flex-direction: column;
  border: 1px solid ${grey[200]};
  border-top: none;
  padding: 0 15px 15px 20px;
`;
const StyledTab = styled(({ ...other }) => (
  <Tab classes={{ label: "label" }} {...other} />
))`
  & .label {
    font-size: 14px;
    color: ${grey[900]};
  }
`;
const Title = styled.h2`
  margin-bottom: 20px;
`;

const Season: React.FC = () => {
  const [selectedTabValue, setSelectedTabValue] = useState<number>(0);
  const { data: season, isLoading, error } = useCurrentSeason();

  const handleChange = (_evt: React.SyntheticEvent, value: number) => {
    setSelectedTabValue(value);
  };

  if (isLoading) {
    return <Title>Loading season...</Title>;
  }

  if (error || !season) {
    return <Title>Error loading season</Title>;
  }

  return (
    <Fragment>
      <Title>Season {season.name}</Title>
      <AppBar position="static" color="default">
        <Tabs value={selectedTabValue} onChange={handleChange}>
          <StyledTab label="Friendly" />
          <StyledTab label="League" />
          <StyledTab label="Statistics" />
        </Tabs>
      </AppBar>
      <TabsContentWrapper>
        {selectedTabValue === 0 && <ScheduleMatch matchType="friendly" />}
        {selectedTabValue === 1 && <ScheduleMatch matchType="league" />}
        {selectedTabValue === 2 && <Statistics />}
      </TabsContentWrapper>
    </Fragment>
  );
};

export default Season;
