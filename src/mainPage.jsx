import React from "react";
import Sidebar from "./component/sidebar/sidebar"
import Header from "./component/header/header"

export default function MainPage() {
    return (
        <React.StrictMode>
            <Header />
            <Sidebar />
        </React.StrictMode>
    )
}
