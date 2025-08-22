import React from "react";
import styled from "styled-components";
import { grey } from "@mui/material/colors";
import { media, responsive } from "./style";
import { PlayerStats } from "./types";
import { usePlayersStats } from "./hooks/useQueries";

const PlayersWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${responsive.spacing.lg};
`;

const PlayersTable = styled.div`
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
`;

const TableTitle = styled.div`
  display: grid;
  font-weight: bold;
  background-color: ${grey[100]};
  align-items: center;
  padding: ${responsive.spacing.md};
  border-radius: 8px 8px 0 0;
  gap: ${responsive.spacing.sm};
  
  /* Mobile: 2 columns (Name, Email) */
  grid-template-columns: 2fr 1fr;
  
  /* Small screens: 3 columns (add Matches) */
  ${media.sm`
    grid-template-columns: 2fr 2fr 1fr;
  `}
  
  /* Medium screens and up: all 4 columns */
  ${media.md`
    grid-template-columns: 2fr 2fr 1fr 1fr;
  `}
`;

const PlayerLine = styled.div`
  display: grid;
  align-items: center;
  padding: ${responsive.spacing.md};
  gap: ${responsive.spacing.sm};
  min-height: ${responsive.touchTarget};
  border-bottom: 1px solid ${grey[200]};
  
  &:nth-child(odd) {
    background-color: ${grey[50]};
  }
  
  &:hover {
    background-color: ${grey[100]};
  }

  /* Mobile: 2 columns (Name, Email) - hide less important data */
  grid-template-columns: 2fr 1fr;
  
  /* Small screens: 3 columns (add Matches) */
  ${media.sm`
    grid-template-columns: 2fr 2fr 1fr;
  `}
  
  /* Medium screens and up: all 4 columns */
  ${media.md`
    grid-template-columns: 2fr 2fr 1fr 1fr;
  `}
`;

const PlayerCell = styled.span`
  word-break: break-word;
  font-size: ${responsive.fontSize.sm};
  
  ${media.md`
    font-size: ${responsive.fontSize.md};
  `}
  
  /* Hide on mobile, show on larger screens */
  &.hide-mobile {
    display: none;
    
    ${media.sm`
      display: block;
    `}
  }
  
  &.hide-small {
    display: none;
    
    ${media.md`
      display: block;
    `}
  }
`;

const TitleWrapper = styled.div``;

const Title = styled.h2`
  margin: 0;
  font-size: ${responsive.fontSize.xl};
  
  ${media.md`
    font-size: ${responsive.fontSize.xxxl};
  `}
`;

const SubTitle = styled.h3`
  margin: ${responsive.spacing.sm} 0;
  font-size: ${responsive.fontSize.lg};
  color: ${grey[600]};
  
  ${media.md`
    font-size: ${responsive.fontSize.xl};
  `}
`;

const Note = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${responsive.spacing.xs};
  padding: ${responsive.spacing.md};
  background-color: ${grey[50]};
  border-radius: 8px;
  font-size: ${responsive.fontSize.sm};
  
  ${media.sm`
    flex-direction: row;
    gap: ${responsive.spacing.lg};
  `}
  
  span {
    color: ${grey[700]};
  }
`;

const Players: React.FC = () => {
  const { data: players = [], isLoading, error } = usePlayersStats();

  const nbLeaguePlayers = players.filter((p: PlayerStats) => p.isPlayerLeague).length;

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
          <PlayerCell>Name</PlayerCell>
          <PlayerCell>Email</PlayerCell>
          <PlayerCell className="hide-mobile">Nb Seasons</PlayerCell>
          <PlayerCell className="hide-small">Nb Matches</PlayerCell>
        </TableTitle>
        {players.sort((p1: PlayerStats, p2: PlayerStats) => p2.nbMatches - p1.nbMatches).map((player: PlayerStats) => (
          <PlayerLine key={player.name}>
            <PlayerCell>
              {player.name} {player.isPlayerLeague && "⭐"}
              {player.isGoalkeeper && " 🥅"}
            </PlayerCell>
            <PlayerCell>{player.email}</PlayerCell>
            <PlayerCell className="hide-mobile">{player.nbSeasons}</PlayerCell>
            <PlayerCell className="hide-small">{player.nbMatches}</PlayerCell>
          </PlayerLine>
        ))}
      </PlayersTable>
    </PlayersWrapper>
  );
};

export default Players;
