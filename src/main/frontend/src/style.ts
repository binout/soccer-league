import { css, CSSObject, FlattenSimpleInterpolation } from 'styled-components';

const sizes: Record<string, number> = {
  phone: 576
};

type MediaFunction = (
  first: TemplateStringsArray | CSSObject,
  ...interpolations: any[]
) => FlattenSimpleInterpolation;

export const media: Record<string, MediaFunction> = Object.keys(sizes).reduce((acc: Record<string, MediaFunction>, label: string) => {
  acc[label] = (...args: Parameters<typeof css>) => css`
    @media (max-width: ${sizes[label] / 16}em) {
      ${css(...args)}
    }
  `;
  return acc;
}, {});