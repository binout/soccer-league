import React, { useState, useEffect } from "react";
import styled from "styled-components";
import axios from "axios";
import grey from "@material-ui/core/colors/grey";
import { media } from "./style";

const PlayersWrapper = styled.div`
  display: flex;
  flex-direction: column;
`;

const PlayersTable = styled.div``;
const TableTitle = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-auto-rows: 35px;
  font-weight: bold;
  background-color: ${grey[100]};
  align-items: center;
  padding-left: 15px;
  margin-bottom: 12px;
`;

const PlayerLine = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-auto-rows: 35px;
  padding-left: 15px;
  align-items: center;
  &:nth-child(odd) {
    background-color: ${grey[100]};
  }

  ${media.phone`
    grid-template-columns: 110px 200px;
    grid-auto-rows: 60px;
    grid-column-gap: 10px;
    `}
`;

const TitleWrapper = styled.div``;

const Title = styled.h2`
  margin: 0;
  font-size: 24px;
`;
const SubTitle = styled.h3`
  font-size: 18px;
`;
const Note = styled.div`
  align-self: flex-end;
  margin: 0 20px 5px 0;
`;

const Players = () => {
  const [players, setPlayers] = useState([]);

  useEffect(() => {
    const fetchPlayers = async () => {
      const result = await axios.get("/rest/players-stats");
      setPlayers(result.data);
    };
    fetchPlayers();
  }, []);

  const nbLeaguePlayers = players.filter(p => p.playerLeague).length;

  return (
    <PlayersWrapper>
      <TitleWrapper>
        <Title>{players.length} Players </Title>
        <SubTitle>{nbLeaguePlayers} League Players</SubTitle>
      </TitleWrapper>
      <Note>
        <span>League Player: "⭐"</span>
        <span>Goalkeeper: "🥅"</span>
      </Note>
      <PlayersTable>
        <TableTitle>
          <span>Name</span>
          <span>Email</span>
          <span>Nb Seasons</span>
          <span>Nb Matches</span>
        </TableTitle>
        {players.sort((p1, p2) => p2.nbMatches - p1.nbMatches).map(player => (
          <PlayerLine key={player.name}>
            <span>
              {player.name} {player.playerLeague && "⭐"}
              {player.goalkeeper && " 🥅"}
            </span>
            <span>{player.email}</span>
            <span>{player.nbSeasons}</span>
            <span>{player.nbMatches}</span>
          </PlayerLine>
        ))}
      </PlayersTable>
    </PlayersWrapper>
  );
};

export default Players;
