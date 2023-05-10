import { createSlice } from "@reduxjs/toolkit";
import configData from '../../config/config.json'
import IsPrivilegedToken from "./IsPrivilegedToken";

export const TokenSlice = createSlice({
    name: 'Token',
    initialState: { isPrivileged: false },
    reducers: {
        check: async (state) => {
            const res = await IsPrivilegedToken();
            state.isPrivileged = res
            console.log("state: " + state.isPrivileged)
        }
    }
})

export const {check} = TokenSlice.actions;
export default TokenSlice.reducer;