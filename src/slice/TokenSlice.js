import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
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

const tokenSlice = createSlice({
    name: 'token',
    initialState: {
        isPrivileged: false,
        isAdmin: false,
    },
    extraReducers: (builder) => {
        builder.addCase(asyncCheckPrivilege.fulfilled, (state, action) => {
            state.isPrivileged = action.payload;
        })
        builder.addCase(asyncCheckAdmin.fulfilled, (state, action) => {
            state.isAdmin = action.payload;
        })
    }
})

export default tokenSlice;
export { asyncCheckAdmin, asyncCheckPrivilege }