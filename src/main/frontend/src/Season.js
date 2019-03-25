import React, { useState, useEffect } from "react";
import styled from "styled-components";
import axios from "axios";

import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import AppBar from "@material-ui/core/AppBar";
import grey from "@material-ui/core/colors/grey";
import ScheduleMatch from "./ScheduleMatch";
import Statistics from "./Statistics";

const TabsContentWrapper = styled.div`
  margin-top: 40px;
`;
const StyledTab = styled(({ ...other }) => (
  <Tab classes={{ label: "label" }} {...other} />
))`
  & .label {
    font-size: 14px;
    color: ${grey[900]};
  }
`;
const Season = () => {
  const [season, setSeason] = useState([]);
  const [selectedTabValue, setSelectedTabValue] = useState(0);

  useEffect(() => {
    const fetchSeasons = async () => {
      const result = await axios("/rest/seasons/current");
      setSeason(result.data);
    };
    fetchSeasons();
  }, []);

  const handleChange = (evt, value) => {
    setSelectedTabValue(value);
  };

  return (
    <div>
      <h2>Season {season.name}</h2>
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
    </div>
  );
};

export default Season;
