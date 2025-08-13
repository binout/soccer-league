import React from 'react';
import { createRoot } from 'react-dom/client';

import Application from './Application.jsx';

const container = document.getElementById('content');
const root = createRoot(container);
window.app = root.render(<Application />);
