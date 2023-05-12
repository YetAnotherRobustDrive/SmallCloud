import { configureStore } from '@reduxjs/toolkit';
import tokenSlice from '../slice/TokenSlice';
import userSlice from '../slice/UserSlice';

 const store = configureStore({
	reducer: { 
		user: userSlice.reducer,
		token: tokenSlice.reducer,
	}
})

export default store;