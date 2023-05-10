import { configureStore } from '@reduxjs/toolkit';
import TokenSlice from '../services/token/TokenSlice';

export const store = configureStore({
	reducer: TokenSlice
})
