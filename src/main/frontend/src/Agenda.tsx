import React, { useState, Fragment } from "react";
import styled from "styled-components";
import AppBar from "@mui/material/AppBar";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import { grey } from "@mui/material/colors";

import PlayersAgenda from "./PlayersAgenda.tsx";

const TabContent = styled.div`
  width: 100%;
  border: 1px solid ${grey[100]};
  padding-top: 30px;
  padding-bottom: 30px;
`;

const StyledTab = styled(({ ...other }) => (
  <Tab classes={{ label: "label" }} {...other} />
))`
  & .label {
    font-size: 14px;
    color: ${grey[900]};
  }
`;

const Agenda: React.FC = () => {
  const [selectedTab, setSelectedTab] = useState<number>(0);
  return (
    <Fragment>
      <AppBar position="static" color="default">
        <Tabs
          value={selectedTab}
          onChange={(_evt: React.SyntheticEvent, value: number) => setSelectedTab(value)}
        >
          <StyledTab label="Friendly" />
          <StyledTab label="League" />
        </Tabs>
      </AppBar>

        <TabContent>
          {selectedTab === 0 && <PlayersAgenda matchType="friendly" />}
          {selectedTab === 1 && <PlayersAgenda matchType="league" />}
        </TabContent>
    </Fragment>
  );
};

export default Agenda;
