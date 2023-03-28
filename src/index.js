import React from 'react';
import { createRoot } from 'react-dom/client';
import Sidebar from './component/sidebar/sidebar';
import './index.css';

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <React.StrictMode>
    <Sidebar />
  </React.StrictMode>
);