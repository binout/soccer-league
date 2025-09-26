import { createTheme, ThemeOptions } from '@mui/material/styles';
import { green } from '@mui/material/colors';
import { designTokens } from './designTokens';

// Common theme settings
const commonTheme: ThemeOptions = {
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    fontWeightRegular: designTokens.fontWeight.regular,
    fontWeightMedium: designTokens.fontWeight.medium,
    fontWeightBold: designTokens.fontWeight.bold,
  },
  shape: {
    borderRadius: parseInt(designTokens.borderRadius.md),
  },
  spacing: 8, // 8px grid system
  breakpoints: {
    values: {
      xs: 0,
      sm: 600,
      md: 900,
      lg: 1200,
      xl: 1536,
    },
  },
  transitions: {
    duration: {
      shortest: 150,
      shorter: 200,
      short: 250,
      standard: 300,
      complex: 375,
      enteringScreen: 225,
      leavingScreen: 195,
    },
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: designTokens.fontWeight.medium,
          minHeight: 44, // Touch target size
          borderRadius: designTokens.borderRadius.md,
          padding: designTokens.components.button.padding,
          transition: designTokens.transition.normal,
        },
      },
    },
    MuiIconButton: {
      styleOverrides: {
        root: {
          minHeight: 44, // Touch target size
          minWidth: 44,
          transition: designTokens.transition.fast,
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          borderRadius: designTokens.borderRadius.lg,
        },
        elevation1: {
          boxShadow: designTokens.elevation.low,
        },
        elevation2: {
          boxShadow: designTokens.elevation.medium,
        },
        elevation3: {
          boxShadow: designTokens.elevation.high,
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: designTokens.components.card.borderRadius,
          padding: designTokens.components.card.padding,
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          padding: designTokens.components.table.cellPadding,
        },
        head: {
          fontWeight: designTokens.components.table.headerFontWeight,
        },
      },
    },
    MuiTab: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: designTokens.fontWeight.medium,
          minHeight: 48,
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          boxShadow: 'none',
        },
      },
    },
  },
};

// Light theme (only theme available)
export const lightTheme = createTheme({
  ...commonTheme,
  palette: {
    mode: 'light',
    primary: {
      main: '#ff6600', // Urban Soccer orange
      light: '#ff8533',
      dark: '#cc5200',
    },
    secondary: {
      main: '#1a1a1a', // Black
      light: '#333333',
      dark: '#000000',
    },
    success: {
      main: '#ff6600', // Orange for success
    },
    warning: {
      main: '#ff9800', // Warning orange
    },
    error: {
      main: '#d32f2f', // Red for errors
    },
    background: {
      default: '#ffffff', // Pure white
      paper: '#fafafa', // Very light gray
    },
    text: {
      primary: '#1a1a1a', // Black
      secondary: '#666666', // Gray
    },
  },
});