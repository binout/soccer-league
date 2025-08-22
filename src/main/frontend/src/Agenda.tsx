import React, { useState, Fragment } from "react";
import styled from "styled-components";
import { 
  AppBar, 
  Tabs, 
  Tab, 
  Box, 
  Paper 
} from "@mui/material";

import PlayersAgenda from "./PlayersAgenda.tsx";

const AgendaContainer = styled(Box)`
  display: flex;
  flex-direction: column;
  gap: 0;
`;

const StyledPaper = styled(Paper)`
  && {
    border-radius: 12px;
    overflow: hidden;
    box-shadow: ${props => props.theme.shadows[2]};
  }
`;

const TabContent = styled(Box)`
  padding: 24px;
`;

const Agenda: React.FC = () => {
  const [selectedTab, setSelectedTab] = useState<number>(0);
  return (
    <AgendaContainer>
      <StyledPaper elevation={0}>
        <AppBar position="static" color="default" elevation={0}>
          <Tabs
            value={selectedTab}
            onChange={(_evt: React.SyntheticEvent, value: number) => setSelectedTab(value)}
            variant="fullWidth"
            indicatorColor="primary"
            textColor="primary"
          >
            <Tab label="⚽ Friendly" />
            <Tab label="🏁 League" />
          </Tabs>
        </AppBar>
        <TabContent>
          {selectedTab === 0 && <PlayersAgenda matchType="friendly" />}
          {selectedTab === 1 && <PlayersAgenda matchType="league" />}
        </TabContent>
      </StyledPaper>
    </AgendaContainer>
  );
};

export default Agenda;
