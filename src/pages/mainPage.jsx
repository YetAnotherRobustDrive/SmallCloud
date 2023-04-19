import React from "react";
import Sidebar from "../component/sidebar/sidebar"
import Header from "../component/header/header"
import BodyHeader from "../component/main/bodyHeader";
import BodyFrame from "../component/bodyframe";

export default function MainPage() {
    return (
        <>
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="공유 파일" />
                <div style={{height:"100px"}}>
                    test
                </div>
                <BodyHeader text="내 파일" />
            </BodyFrame>
        </>
    )
}
