import React, {Fragment, useState, useEffect} from 'react';
import styled from 'styled-components';
import grey from '@material-ui/core/colors/grey';
import axios from 'axios';


const StatisticsHeader = styled.div`
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
const LineByPlayer = styled.div`
  display: grid;
  grid-template-columns: [first] 30% 20% 20% 20%;
  grid-auto-rows: 35px;
  align-items: center;
  border-bottom: 1px solid ${grey[200]};
`;


const Statistics = () => {

  const [stats, setStats] = useState([])

  useEffect(() => {
    async function fetchData() {
      console.log('fetchStats')
      const result = await axios('/rest/seasons/current/stats');
      setStats(result.data);
    }

    fetchData();
  }, []);

  return (
    <Fragment>
    <StatisticsHeader>
      <div>Player</div>
      <div>Friendly</div>
      <div>League</div>
      <div>Total Matches Played</div>
    </StatisticsHeader>
    <StatisticsBody>
      {stats.map(stat => (
        <LineByPlayer key={stat.player}>
        <span>{stat.player}</span>
        <span>{stat.nbFriendlyMatches}</span>
        <span>{stat.nbLeagueMatches}</span>
        <span>{stat.nbMatches}</span>
        </LineByPlayer>
      ))}
    </StatisticsBody>
  </Fragment>
  )
}

export default Statistics;
