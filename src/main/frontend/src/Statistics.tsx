import React, { Fragment } from "react";
import styled from "styled-components";
import { grey } from "@mui/material/colors";
import { StatisticsData } from "./types";
import { useSeasonStats } from "./hooks/useQueries";

const StatisticsHeader = styled.div`
  padding-top: 15px;
  display: grid;
  grid-template-columns: [first] 30% 20% 20% 20%;
  grid-auto-rows: 40px;
  align-items: center;
`;

const StatisticsBody = styled.div`
  display: grid;
  div:first-child {
    border-top: 2px solid ${grey[200]};
  }
  div:last-child {
    border: none;
  }
`;

const Title = styled.div`
  font-weight: bold;
  font-size: 16px;
`;

const LineByPlayer = styled.div`
  display: grid;
  grid-template-columns: [first] 30% 20% 20% 20%;
  grid-auto-rows: 35px;
  grid-column-gap: 15px;
  align-items: center;
  border-bottom: 1px solid ${grey[200]};
  &:nth-child(odd) {
    background-color: ${grey[100]};
  }
`;

const Statistics: React.FC = () => {
  const { data: stats = [], isLoading, error } = useSeasonStats();

  if (isLoading) {
    return <div>Loading statistics...</div>;
  }

  if (error) {
    return <div>Error loading statistics</div>;
  }

  return (
    <Fragment>
      <StatisticsHeader>
        <Title style={{ paddingLeft: "15px" }}>Player</Title>
        <Title>Friendly</Title>
        <Title>League</Title>
        <Title>Total Matches Played</Title>
      </StatisticsHeader>
      <StatisticsBody>
        {stats.map((stat: StatisticsData) => (
          <LineByPlayer key={stat.player}>
            <span style={{ paddingLeft: "15px" }}>{stat.player}</span>
            <span>{stat.nbFriendlyMatches}</span>
            <span>{stat.nbLeagueMatches}</span>
            <span>{stat.nbMatches}</span>
          </LineByPlayer>
        ))}
      </StatisticsBody>
    </Fragment>
  );
};

export default Statistics;