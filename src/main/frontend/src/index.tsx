import React from 'react';
import { createRoot } from 'react-dom/client';

import Application from './Application.tsx';

const container = document.getElementById('content');
if (!container) {
  throw new Error('Failed to find the root element');
}

const root = createRoot(container);
(window as any).app = root.render(<Application />);
