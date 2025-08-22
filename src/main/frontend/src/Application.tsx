import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, NavLink as RouterNavLink } from "react-router-dom";
import { QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import styled, { ThemeProvider as StyledThemeProvider } from "styled-components";
import { createGlobalStyle } from "styled-components";
import AppBar from "@mui/material/AppBar";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import CloseIcon from "@mui/icons-material/Close";
import { green } from "@mui/material/colors";
import { useTheme as useMuiTheme } from "@mui/material/styles";
import Players from "./Players.tsx";
import Agenda from "./Agenda.tsx";
import Season from "./Season.tsx";
import { Toolbar } from "@mui/material";
import { media, responsive } from "./style";
import { queryClient } from "./config/queryClient";
import SwipeNavigation from "./components/SwipeNavigation";
import { CustomThemeProvider } from "./contexts/ThemeContext";
import ThemeToggle from "./components/ThemeToggle";

const GlobalStyle = createGlobalStyle<{ theme: any }>`
  * {
    box-sizing: border-box;
  }

  body {
    margin: 0;
    font-family: "Roboto", "Helvetica", "Arial", sans-serif;
    line-height: 1.5;
    background-color: ${props => props.theme.palette.background.default};
    color: ${props => props.theme.palette.text.primary};
    transition: background-color 0.3s ease, color 0.3s ease;
    
    /* Mobile-first typography */
    font-size: ${responsive.fontSize.md};
    
    ${media.md`
      font-size: ${responsive.fontSize.lg};
    `}
  }

  /* Ensure touch targets are at least 44px */
  button, 
  a,
  input[type="button"],
  input[type="submit"] {
    min-height: ${responsive.touchTarget};
    min-width: ${responsive.touchTarget};
  }
`;

const SoccerAppWrapper = styled.div`
  margin: 0 auto;
`;

const StyledAppBar = styled(AppBar)<{ theme: any }>`
  && {
    background-color: ${props => props.theme.palette.primary.main};
    margin-bottom: ${responsive.spacing.lg};
    transition: background-color 0.3s ease;
    
    ${media.md`
      margin-bottom: ${responsive.spacing.xxl};
    `}
  }
`;

const StyledToolbar = styled(Toolbar)`
  && {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: ${responsive.spacing.sm};
    
    ${media.sm`
      justify-content: flex-start;
      padding: ${responsive.spacing.md};
    `}
  }
`;

const MobileMenuButton = styled(IconButton)`
  && {
    color: white;
    padding: ${responsive.spacing.sm};
    
    ${media.sm`
      display: none;
    `}
  }
`;

const NavigationContainer = styled.div<{ $isOpen: boolean; theme: any }>`
  /* Mobile: overlay menu */
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: ${props => props.theme.palette.primary.main};
  z-index: 1300;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: ${responsive.spacing.lg};
  transform: translateX(${props => props.$isOpen ? '0' : '-100%'});
  transition: transform 0.3s ease-in-out, background-color 0.3s ease;
  
  ${media.sm`
    /* Desktop: horizontal navigation - always visible */
    position: static;
    background: none;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    gap: 0;
    transform: translateX(0) !important;
    transition: none;
    z-index: auto;
    width: auto;
    height: auto;
    top: auto;
    left: auto;
    right: auto;
    bottom: auto;
  `}
`;

const MobileMenuHeader = styled.div`
  position: absolute;
  top: 0;
  right: 0;
  padding: ${responsive.spacing.md};
  
  ${media.sm`
    display: none;
  `}
`;

const BrandLink = styled(RouterNavLink)`
  color: white;
  text-decoration: none;
  font-size: ${responsive.fontSize.lg};
  font-weight: 600;
  
  ${media.sm`
    margin-right: ${responsive.spacing.xl};
  `}
  
  &:hover {
    text-decoration: none;
    color: white;
  }
`;

const StyledNavLink = styled(RouterNavLink)`
  color: white;
  text-decoration: none;
  text-transform: uppercase;
  font-weight: 500;
  transition: all 0.3s ease;
  opacity: 0.9;
  
  /* Mobile: large touch targets in overlay menu */
  display: block;
  width: 100%;
  text-align: center;
  padding: ${responsive.spacing.lg};
  font-size: ${responsive.fontSize.xl};
  min-height: ${responsive.touchTarget};
  border-radius: 8px;
  margin: ${responsive.spacing.xs} 0;
  
  /* Small screens and up: horizontal layout */
  ${media.sm`
    display: inline-block;
    width: auto;
    padding: ${responsive.spacing.lg} ${responsive.spacing.xl};
    font-size: ${responsive.fontSize.lg};
    margin: 0;
    border-radius: 4px;
  `}

  &:active,
  &:visited,
  &:focus,
  &:hover {
    text-decoration: none;
    color: white;
    opacity: 1;
    background-color: rgba(255, 255, 255, 0.1);
    transform: scale(1.05);
  }

  &.active {
    opacity: 1;
    background-color: rgba(255, 255, 255, 0.2);
    transform: scale(1.02);
  }
  
  /* Touch feedback enhancement */
  &:active {
    transform: scale(0.98);
  }
`;

const Content = styled.div`
  /* Mobile-first: full width with padding */
  width: 100%;
  padding: 0 ${responsive.spacing.md};
  margin: 0 auto;
  
  /* Small screens: add more padding */
  ${media.sm`
    padding: 0 ${responsive.spacing.lg};
  `}
  
  /* Medium screens and up: constrain width */
  ${media.md`
    width: 90%;
    padding: 0 ${responsive.spacing.xl};
  `}
  
  /* Large screens: further constrain width */
  ${media.lg`
    width: 85%;
    max-width: 1200px;
  `}
  
  /* Extra large screens: maximum constraint */
  ${media.xl`
    width: 80%;
    max-width: 1400px;
  `}
`;

const ApplicationContent: React.FC = () => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const menuButtonRef = React.useRef<HTMLButtonElement>(null);
  const firstMenuLinkRef = React.useRef<HTMLAnchorElement>(null);
  const muiTheme = useMuiTheme();

  const toggleMobileMenu = () => {
    setMobileMenuOpen(!mobileMenuOpen);
  };

  const closeMobileMenu = () => {
    setMobileMenuOpen(false);
    // Return focus to menu button after closing
    setTimeout(() => {
      menuButtonRef.current?.focus();
    }, 100);
  };

  // Close menu on route change (for mobile)
  const handleNavLinkClick = () => {
    closeMobileMenu();
  };

  // Handle keyboard navigation for mobile menu
  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Escape') {
      closeMobileMenu();
    }
  };

  // Add body scroll lock when menu is open and manage focus
  React.useEffect(() => {
    if (mobileMenuOpen) {
      document.body.style.overflow = 'hidden';
      // Focus first menu link when menu opens
      setTimeout(() => {
        firstMenuLinkRef.current?.focus();
      }, 100);
    } else {
      document.body.style.overflow = 'unset';
    }
    
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, [mobileMenuOpen]);

  return (
    <StyledThemeProvider theme={muiTheme}>
      <QueryClientProvider client={queryClient}>
        <Router>
          <GlobalStyle theme={muiTheme} />
          <SoccerAppWrapper>
            <StyledAppBar position="static" color="default" theme={muiTheme}>
              <StyledToolbar>
                <BrandLink to="/" onClick={handleNavLinkClick}>
                  Planning Equipe Soccer 5
                </BrandLink>
                
                <NavigationContainer 
                  $isOpen={mobileMenuOpen}
                  theme={muiTheme}
                  role="navigation"
                  aria-label="Navigation menu"
                  onKeyDown={handleKeyDown}
                  id="navigation-menu"
                >
                  <MobileMenuHeader>
                    <IconButton 
                      onClick={closeMobileMenu}
                      aria-label="Close navigation menu"
                      sx={{ color: 'white', padding: '0.5rem' }}
                    >
                      <CloseIcon />
                    </IconButton>
                  </MobileMenuHeader>
                  
                  <StyledNavLink 
                    ref={firstMenuLinkRef}
                    to="/" 
                    onClick={handleNavLinkClick}
                  >
                    Season
                  </StyledNavLink>
                  <StyledNavLink 
                    to="/agenda" 
                    onClick={handleNavLinkClick}
                  >
                    Agenda
                  </StyledNavLink>
                  <StyledNavLink 
                    to="/players" 
                    onClick={handleNavLinkClick}
                  >
                    Players
                  </StyledNavLink>
                </NavigationContainer>
                
                <div style={{ marginLeft: 'auto' }}>
                  <ThemeToggle />
                </div>
                <MobileMenuButton
                  ref={menuButtonRef}
                  onClick={toggleMobileMenu}
                  aria-label="Toggle navigation menu"
                  aria-expanded={mobileMenuOpen}
                  aria-controls="navigation-menu"
                >
                  <MenuIcon />
                </MobileMenuButton>
              </StyledToolbar>
            </StyledAppBar>
            <Content>
              <SwipeNavigation>
                <Routes>
                  <Route path="/" element={<Season />} />
                  <Route path="/agenda" element={<Agenda />} />
                  <Route path="/players" element={<Players />} />
                </Routes>
              </SwipeNavigation>
            </Content>
          </SoccerAppWrapper>
        </Router>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </StyledThemeProvider>
  );
};

const Application: React.FC = () => {
  return (
    <CustomThemeProvider>
      <ApplicationContent />
    </CustomThemeProvider>
  );
};

export default Application;
