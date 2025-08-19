// Type definitions for the Soccer League application

export interface Player {
  name: string;
  email?: string;
}

export interface PlayerStats {
  name: string;
  email: string;
  isPlayerLeague: boolean;
  isGoalkeeper: boolean;
  nbSeasons: number;
  nbMatches: number;
}

export interface Season {
  name: string;
}

export interface MatchDate {
  date: string;
  presents: string[];
  canBePlanned: boolean;
}

export interface Match {
  date: string;
  players: string[];
  subs: string[];
  hasMinimumPlayer: boolean;
}

export interface MatchToPlan {
  date: string;
}

export interface StatisticsData {
  player: string;
  nbFriendlyMatches: number;
  nbLeagueMatches: number;
  nbMatches: number;
}

// Utility type for array operations
export type ArrayElement<ArrayType extends readonly unknown[]> = ArrayType extends readonly (infer ElementType)[] ? ElementType : never;

// API response types
export interface ApiResponse<T> {
  data: T;
}