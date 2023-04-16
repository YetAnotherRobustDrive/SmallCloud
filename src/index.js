import React from 'react';
import { createRoot } from 'react-dom/client';
import MainPage from './mainPage.jsx'
import './index.css';
import LoginPage from './component/login/loginPage'

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
  <React.StrictMode>
    <LoginPage />
  </React.StrictMode>
);