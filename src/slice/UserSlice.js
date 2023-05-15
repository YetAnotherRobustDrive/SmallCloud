import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { PURGE } from "redux-persist";
import GetUserInfo from "../services/user/GetUserInfo";

const asyncGetUserInfo = createAsyncThunk(
    'tokenSlice/asyncGetUserInfo',
    async () => {
        return await GetUserInfo();
    }
)

const initialState = {
    isLoggedIn: false,
}

const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers:{
        setIsLoggedIn: (state, action) => {
            state.isLoggedIn = true;
            state.loginID = action.payload;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(asyncGetUserInfo.fulfilled, (state, action) => {
            state.nickname = action.payload;
        });
        builder.addCase(PURGE, () => initialState);
    }
})

export default userSlice;
export const { setIsLoggedIn } = userSlice.actions;
export {asyncGetUserInfo}