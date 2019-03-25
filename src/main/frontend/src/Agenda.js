import React, { useState, Fragment } from "react";
import styled from "styled-components";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import grey from "@material-ui/core/colors/grey";

import PlayersAgenda from "./PlayersAgenda.js";

const TabContent = styled.div`
  width: 100%;
  overflow: auto;
`;

const StyledTab = styled(({ ...other }) => (
  <Tab classes={{ label: "label" }} {...other} />
))`
  & .label {
    font-size: 14px;
    color: ${grey[900]};
  }
`;
const Agenda = () => {
  const [selectedTab, setSelectedTab] = useState(0);
  return (
    <Fragment>
      <Tabs
        value={selectedTab}
        onChange={(evt, value) => setSelectedTab(value)}
      >
        <StyledTab label="Friendly" />
        <StyledTab label="League" />
      </Tabs>
      <TabContent>
        {selectedTab === 0 && <PlayersAgenda matchType="friendly" />}
        {selectedTab === 1 && <PlayersAgenda matchType="league" />}
      </TabContent>
    </Fragment>
  );
};

export default Agenda;
