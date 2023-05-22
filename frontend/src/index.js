import React from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import { Provider } from 'react-redux';
import store from './store/store'
import ConditionalRoute from './pages/ConditionalRoute';
import { PersistGate } from 'redux-persist/integration/react';
import persistStore from 'redux-persist/es/persistStore';

const container = document.getElementById('root');
const root = createRoot(container);
export let persistor = persistStore(store);

root.render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <ConditionalRoute />
    </PersistGate>
  </Provider>
);