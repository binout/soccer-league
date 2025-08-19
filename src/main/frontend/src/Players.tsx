import React from "react";
import styled from "styled-components";
import { grey } from "@mui/material/colors";
import { media } from "./style";
import { PlayerStats } from "./types";
import { usePlayersStats } from "./hooks/useQueries";

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
    grid-template-columns: repeat(4, 1fr);
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

const Players: React.FC = () => {
  const { data: players = [], isLoading, error } = usePlayersStats();

  const nbLeaguePlayers = players.filter((p: PlayerStats) => p.playerLeague).length;

  if (isLoading) {
    return (
      <PlayersWrapper>
        <TitleWrapper>
          <Title>Loading players...</Title>
        </TitleWrapper>
      </PlayersWrapper>
    );
  }

  if (error) {
    return (
      <PlayersWrapper>
        <TitleWrapper>
          <Title>Error loading players</Title>
        </TitleWrapper>
      </PlayersWrapper>
    );
  }

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
        {players.sort((p1: PlayerStats, p2: PlayerStats) => p2.nbMatches - p1.nbMatches).map((player: PlayerStats) => (
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
