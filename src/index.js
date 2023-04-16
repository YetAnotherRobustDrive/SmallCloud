import React from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import Routers from './component/router'

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <React.StrictMode>
    <Routers/>
  </React.StrictMode>
);