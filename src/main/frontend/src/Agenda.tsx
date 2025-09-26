import React, { useState, Fragment } from "react";
import styled from "styled-components";
import {
  Tabs,
  Tab,
  Box,
  Paper,
  AppBar,
  Typography
} from "@mui/material";
import { responsive, media } from "./style";

import PlayersAgenda from "./PlayersAgenda.tsx";

const AgendaContainer = styled(Box)`
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

const Agenda: React.FC = () => {
  const [selectedTab, setSelectedTab] = useState<number>(0);
  return (
    <AgendaContainer>
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
        📅 Match Planning
      </Typography>

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
        <TabsContentWrapper>
          {selectedTab === 0 && <PlayersAgenda matchType="friendly" />}
          {selectedTab === 1 && <PlayersAgenda matchType="league" />}
        </TabsContentWrapper>
      </StyledPaper>
    </AgendaContainer>
  );
};

export default Agenda;
