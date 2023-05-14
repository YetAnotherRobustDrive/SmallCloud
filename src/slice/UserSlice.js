import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import GetUserInfo from "../services/user/GetUserInfo";

const asyncGetUserInfo = createAsyncThunk(
    'tokenSlice/asyncGetUserInfo',
    async () => {
        return await GetUserInfo();
    }
)

const userSlice = createSlice({
    name: 'user',
    initialState: {
        isLoggedIn: false,
    },
    reducers:{
        setIsLoggedIn: (state, action) => {
            state.isLoggedIn = true;
            state.loginID = action.payload;
        }
    },
    extraReducers: (builder) => {
        builder.addCase(asyncGetUserInfo.fulfilled, (state, action) => {
            state.nickname = action.payload;
        })
    }
})

export default userSlice;
export const { setIsLoggedIn } = userSlice.actions;
export {asyncGetUserInfo}