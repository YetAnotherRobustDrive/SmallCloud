import { createSlice } from "@reduxjs/toolkit";

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
})

export default userSlice;
export const { setIsLoggedIn } = userSlice.actions;