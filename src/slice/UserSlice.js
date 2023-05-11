import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import IsPrivilegedToken from "../services/token/IsPrivilegedToken";

const asyncSetPrivilege = createAsyncThunk(
    'userSlice/asyncSetPrivilege',
    async () => {
        return await IsPrivilegedToken();
    }
)

const userSlice = createSlice({
    name: 'user',
    initialState: {
        isPrivileged: false,
        nickname: 'Error...',
    },
    extraReducers: (builder) => {
        builder.addCase(asyncSetPrivilege.fulfilled, (state, action) => {
            state.isPrivileged = action.payload;
        })
    }
})

export default userSlice;
export { asyncSetPrivilege }