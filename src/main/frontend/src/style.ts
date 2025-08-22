import { css, CSSObject } from 'styled-components';

// Mobile-first breakpoints (min-width approach)
const breakpoints: Record<string, number> = {
  sm: 576,   // Small devices (landscape phones)
  md: 768,   // Medium devices (tablets)  
  lg: 992,   // Large devices (desktops)
  xl: 1200,  // Extra large devices (large desktops)
  xxl: 1400  // Extra extra large devices
};

type MediaFunction = (
  first: TemplateStringsArray | CSSObject,
  ...interpolations: any[]
) => ReturnType<typeof css>;

// Mobile-first media queries (min-width)
export const media: Record<string, MediaFunction> = Object.keys(breakpoints).reduce((acc: Record<string, MediaFunction>, label: string) => {
  acc[label] = (...args: Parameters<typeof css>) => css`
    @media (min-width: ${breakpoints[label]}px) {
      ${css(...args)}
    }
  `;
  return acc;
}, {});

// Legacy support for existing 'phone' breakpoint (max-width for backwards compatibility)
media.phone = (...args: Parameters<typeof css>) => css`
  @media (max-width: 575px) {
    ${css(...args)}
  }
`;

// Utility for responsive values
export const responsive = {
  // Touch-friendly sizing
  touchTarget: '44px',
  
  // Spacing scale
  spacing: {
    xs: '0.25rem',   // 4px
    sm: '0.5rem',    // 8px  
    md: '1rem',      // 16px
    lg: '1.5rem',    // 24px
    xl: '2rem',      // 32px
    xxl: '3rem',     // 48px
  },
  
  // Typography scale  
  fontSize: {
    xs: '0.75rem',   // 12px
    sm: '0.875rem',  // 14px
    md: '1rem',      // 16px
    lg: '1.125rem',  // 18px
    xl: '1.25rem',   // 20px
    xxl: '1.5rem',   // 24px
    xxxl: '2rem',    // 32px
  }
};