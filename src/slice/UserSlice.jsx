import { createSlice } from "@reduxjs/toolkit";

export const UserSlice = createSlice({
    name: 'user',
    initialState: { 
        isPrivileged: false,
        nickname: 'Error...',
    },
    reducers: {
        setPrivilege: (state, action) => {
            state.isPrivileged = action.payload.res;
        },
        setNickname: (state, action) => {
            state.nickname = action.payload.res;
        }
        
    }
})

export const { setPrivilege } = UserSlice.actions;
export default UserSlice.reducer;

export const selectIsPrivileged = (state) => state.token.isPrivileged;
export const selectNickname = (state) => state.token.nickname;