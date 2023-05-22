import { createSlice } from "@reduxjs/toolkit";
import { PURGE } from "redux-persist";

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
        builder.addCase(PURGE, () => initialState);
    }
})

export default userSlice;
export const { setIsLoggedIn } = userSlice.actions;