import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { apiService } from '../services/api';

// Query keys
export const queryKeys = {
  season: ['season'] as const,
  seasonStats: ['season', 'stats'] as const,
  playersStats: ['players', 'stats'] as const,
  players: ['players'] as const,
  leaguePlayers: ['players', 'league'] as const,
  matchDates: (matchType: 'friendly' | 'league') => ['matchDates', matchType] as const,
  matches: (matchType: 'friendly' | 'league', date: string) => ['matches', matchType, date] as const,
  matchesToPlan: (matchType: 'friendly' | 'league', date: string) => ['matchesToPlan', matchType, date] as const,
};

// Season hooks
export const useCurrentSeason = () => {
  return useQuery({
    queryKey: queryKeys.season,
    queryFn: apiService.getCurrentSeason,
  });
};

export const useSeasonStats = () => {
  return useQuery({
    queryKey: queryKeys.seasonStats,
    queryFn: apiService.getSeasonStats,
  });
};

// Players hooks
export const usePlayersStats = () => {
  return useQuery({
    queryKey: queryKeys.playersStats,
    queryFn: apiService.getPlayersStats,
  });
};

export const usePlayers = () => {
  return useQuery({
    queryKey: queryKeys.players,
    queryFn: apiService.getPlayers,
  });
};

export const useLeaguePlayers = () => {
  return useQuery({
    queryKey: queryKeys.leaguePlayers,
    queryFn: apiService.getLeaguePlayers,
  });
};

// Match dates hooks
export const useMatchDates = (matchType: 'friendly' | 'league') => {
  return useQuery({
    queryKey: queryKeys.matchDates(matchType),
    queryFn: () => apiService.getMatchDates(matchType),
  });
};

// Match planning hooks
export const useMatches = (matchType: 'friendly' | 'league') => {
  return useQuery({
    queryKey: queryKeys.matches(matchType, 'next'),
    queryFn: () => apiService.getMatches(matchType),
  });
};

export const useMatchesToPlan = (matchType: 'friendly' | 'league') => {
  return useQuery({
    queryKey: queryKeys.matchesToPlan(matchType, 'to-plan'),
    queryFn: () => apiService.getMatchesToPlan(matchType),
  });
};

// Mutation hooks
export const useCreateMatchDate = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ matchType, date }: { matchType: 'friendly' | 'league'; date: string }) =>
      apiService.createMatchDate(matchType, date),
    onSuccess: () => {
      queryClient.invalidateQueries();
    },
  });
};

export const usePlanMatch = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ matchType, date }: { matchType: 'friendly' | 'league'; date: string }) =>
      apiService.planMatch(matchType, date),
    onSuccess: () => {
      queryClient.invalidateQueries();
    },
  });
};

export const usePlayerPresence = () => {
  const queryClient = useQueryClient();
  
  return {
    add: useMutation({
      mutationFn: ({ matchType, date, playerId }: { matchType: 'friendly' | 'league'; date: string; playerId: string }) =>
        apiService.addPlayerPresence(matchType, date, playerId),
      onSuccess: () => {
        queryClient.invalidateQueries();
      },
    }),
    
    remove: useMutation({
      mutationFn: ({ matchType, date, playerId }: { matchType: 'friendly' | 'league'; date: string; playerId: string }) =>
        apiService.removePlayerPresence(matchType, date, playerId),
      onSuccess: () => {
        queryClient.invalidateQueries();
      },
    }),
  };
};

export const useSubstitutePlayer = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ matchType, date, playerId }: { 
      matchType: 'friendly' | 'league'; 
      date: string; 
      playerId: string;
    }) => apiService.substitutePlayer(matchType, date, playerId),
    onSuccess: () => {
      queryClient.invalidateQueries();
    },
  });
};