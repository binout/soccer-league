// Design tokens for consistent spacing, typography, and styling
export const designTokens = {
  // Spacing system (8px grid)
  spacing: {
    xs: '4px',
    sm: '8px',
    md: '16px',
    lg: '24px',
    xl: '32px',
    xxl: '48px',
    xxxl: '64px',
  },
  
  // Border radius
  borderRadius: {
    sm: '4px',
    md: '8px',
    lg: '12px',
    xl: '16px',
    round: '50%',
  },
  
  // Shadows (matching MUI theme)
  elevation: {
    none: 'none',
    low: '0px 2px 4px rgba(0, 0, 0, 0.1)',
    medium: '0px 4px 8px rgba(0, 0, 0, 0.12)',
    high: '0px 8px 16px rgba(0, 0, 0, 0.15)',
  },
  
  // Typography weights
  fontWeight: {
    regular: 400,
    medium: 500,
    semibold: 600,
    bold: 700,
  },
  
  // Transitions
  transition: {
    fast: '0.15s ease',
    normal: '0.3s ease',
    slow: '0.5s ease',
  },
  
  // Z-index scale
  zIndex: {
    base: 0,
    dropdown: 1000,
    sticky: 1020,
    fixed: 1030,
    modal: 1040,
    tooltip: 1070,
  },
  
  // Component specific tokens
  components: {
    card: {
      padding: '24px',
      borderRadius: '12px',
      elevation: 'medium',
    },
    button: {
      borderRadius: '8px',
      padding: '12px 24px',
      fontWeight: 500,
    },
    input: {
      borderRadius: '8px',
      padding: '12px 16px',
    },
    table: {
      headerFontWeight: 600,
      cellPadding: '16px',
      borderRadius: '12px',
    },
  },
} as const;

export type DesignTokens = typeof designTokens;