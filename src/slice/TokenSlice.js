import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { PURGE } from "redux-persist";
import IsAdminToken from "../services/token/IsAdminToken";
import IsPrivilegedToken from "../services/token/IsPrivilegedToken";

const asyncCheckPrivilege = createAsyncThunk(
    'tokenSlice/asyncCheckPrivilege',
    async () => {
        return await IsPrivilegedToken();
    }
)

const asyncCheckAdmin = createAsyncThunk(
    'tokenSlice/asyncCheckAdmin',
    async () => {
        return await IsAdminToken();
    }
)

const initialState = {
    isPrivileged: false,
    isAdmin: false,
}

const tokenSlice = createSlice({
    name: 'token',
    initialState,
    extraReducers: (builder) => {
        builder.addCase(asyncCheckPrivilege.fulfilled, (state, action) => {
            state.isPrivileged = action.payload;
        });
        builder.addCase(asyncCheckAdmin.fulfilled, (state, action) => {
            state.isAdmin = action.payload;
        });
        builder.addCase(PURGE, () => initialState);
    }
})

export default tokenSlice;
export { asyncCheckAdmin, asyncCheckPrivilege }