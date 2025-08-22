import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import { useSwipeGesture } from '../hooks/useSwipeGesture';
import { responsive } from '../style';

interface SwipeNavigationProps {
  children: React.ReactNode;
}

const SwipeContainer = styled.div`
  position: relative;
  width: 100%;
  min-height: 100%;
  touch-action: pan-y; /* Allow vertical scrolling but handle horizontal swipes */
`;

const SwipeIndicator = styled.div<{ show: boolean }>`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: ${responsive.spacing.lg};
  border-radius: 8px;
  font-size: ${responsive.fontSize.lg};
  font-weight: 500;
  z-index: 9999;
  opacity: ${props => props.show ? 1 : 0};
  visibility: ${props => props.show ? 'visible' : 'hidden'};
  transition: all 0.3s ease;
  pointer-events: none;
`;

const SwipeNavigation: React.FC<SwipeNavigationProps> = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showIndicator, setShowIndicator] = React.useState(false);
  const [indicatorText, setIndicatorText] = React.useState('');

  // Define the navigation routes in order
  const routes = ['/', '/agenda', '/players'];
  const routeNames = ['Season', 'Agenda', 'Players'];

  const currentIndex = routes.indexOf(location.pathname);

  const showSwipeIndicator = (text: string) => {
    setIndicatorText(text);
    setShowIndicator(true);
    setTimeout(() => setShowIndicator(false), 1000);
  };

  const handleSwipeLeft = () => {
    // Swipe left = go to next route
    if (currentIndex < routes.length - 1) {
      const nextRoute = routes[currentIndex + 1];
      const nextName = routeNames[currentIndex + 1];
      showSwipeIndicator(`→ ${nextName}`);
      setTimeout(() => navigate(nextRoute), 200);
    }
  };

  const handleSwipeRight = () => {
    // Swipe right = go to previous route
    if (currentIndex > 0) {
      const prevRoute = routes[currentIndex - 1];
      const prevName = routeNames[currentIndex - 1];
      showSwipeIndicator(`← ${prevName}`);
      setTimeout(() => navigate(prevRoute), 200);
    }
  };

  const swipeRef = useSwipeGesture({
    onSwipeLeft: handleSwipeLeft,
    onSwipeRight: handleSwipeRight,
    threshold: 100, // Require a longer swipe to avoid accidental navigation
    preventScroll: false // Allow vertical scrolling
  });

  return (
    <SwipeContainer ref={swipeRef}>
      {children}
      <SwipeIndicator show={showIndicator}>
        {indicatorText}
      </SwipeIndicator>
    </SwipeContainer>
  );
};

export default SwipeNavigation;