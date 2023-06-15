import { combineReducers, configureStore } from '@reduxjs/toolkit';
import { persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';
import tokenSlice from '../slice/TokenSlice';
import userSlice from '../slice/UserSlice';

const reducers = combineReducers({
	user: userSlice.reducer,
	token: tokenSlice.reducer,
})

const persistConfig = {
	key: 'root',
	storage,
};

const persistedReducer = persistReducer(persistConfig, reducers);

const store = configureStore({
	reducer: persistedReducer,
	middleware: getDefaultMiddleware => getDefaultMiddleware({ serializableCheck: false }),
})

export default store;