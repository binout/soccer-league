import React, { useState, useEffect, Fragment } from "react";
import styled from "styled-components";
import axios from "axios";

import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import AppBar from "@material-ui/core/AppBar";
import grey from "@material-ui/core/colors/grey";
import ScheduleMatch from "./ScheduleMatch";
import Statistics from "./Statistics";

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
