import { createSlice } from "@reduxjs/toolkit";

export const TokenSlice = createSlice({
    name: 'token',
    initialState: { isPrivileged: false },
    reducers: {
        setPrivilege: (state, action) => {
            const res = action.payload.res;
            state.isPrivileged = res;
        }
    }
})

export const { setPrivilege } = TokenSlice.actions;
export default TokenSlice.reducer;

export const selectIsPrivileged = (state) => state.token.isPrivileged;