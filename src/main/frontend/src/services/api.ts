import axios from 'axios';
import { PlayerStats, Player, MatchDate, Match, MatchToPlan, Season, StatisticsData } from '../types';

const api = axios.create({
  baseURL: '/rest',
});

export const apiService = {
  // Season endpoints
  getCurrentSeason: async (): Promise<Season> => {
    const response = await api.get<Season>('/seasons/current');
    return response.data;
  },

  getSeasonStats: async (): Promise<StatisticsData[]> => {
    const response = await api.get<StatisticsData[]>('/seasons/current/stats');
    return response.data;
  },

  // Players endpoints
  getPlayersStats: async (): Promise<PlayerStats[]> => {
    const response = await api.get<PlayerStats[]>('/players-stats');
    return response.data;
  },

  getPlayers: async (): Promise<Player[]> => {
    const response = await api.get<Player[]>('/players');
    return response.data;
  },

  getLeaguePlayers: async (): Promise<Player[]> => {
    const response = await api.get<Player[]>('/players/league');
    return response.data;
  },

  // Match dates endpoints
  getMatchDates: async (matchType: 'friendly' | 'league'): Promise<MatchDate[]> => {
    const response = await api.get<MatchDate[]>(`/match-dates/${matchType}/next`);
    return response.data;
  },

  createMatchDate: async (matchType: 'friendly' | 'league', date: string): Promise<void> => {
    await api.put(`/match-dates/${matchType}/${date}`);
  },

  // Match planning endpoints
  getMatches: async (matchType: 'friendly' | 'league'): Promise<Match[]> => {
    const response = await api.get<Match[]>(`/seasons/current/matches/${matchType}/next`);
    return response.data;
  },

  getMatchesToPlan: async (matchType: 'friendly' | 'league'): Promise<MatchToPlan[]> => {
    const response = await api.get<MatchToPlan[]>(`/seasons/current/matches/${matchType}/to-plan`);
    return response.data;
  },

  planMatch: async (matchType: 'friendly' | 'league', date: string): Promise<void> => {
    await api.put(`/seasons/current/matches/${matchType}/${date}`);
  },

  // Player presence management
  addPlayerPresence: async (matchType: 'friendly' | 'league', date: string, playerId: string): Promise<void> => {
    await api.put(`/match-dates/${matchType}/${date}/players/${playerId}`);
  },

  removePlayerPresence: async (matchType: 'friendly' | 'league', date: string, playerId: string): Promise<void> => {
    await api.delete(`/match-dates/${matchType}/${date}/players/${playerId}`);
  },

  substitutePlayer: async (matchType: 'friendly' | 'league', date: string, playerId: string): Promise<void> => {
    await api.delete(`/seasons/current/matches/${matchType}/${date}/players/${playerId}`);
  },
};

export default apiService;