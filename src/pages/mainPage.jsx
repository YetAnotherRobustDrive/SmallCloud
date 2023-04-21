import React from "react";
import Sidebar from "../component/sidebar/sidebar"
import Header from "../component/header/header"
import BodyHeader from "../component/main/bodyHeader";
import BodyFrame from "../component/bodyframe";
import NarrowBox from "../component/main/narrowBox";
import CustomIcon from "../component/main/customIcon";

export default function MainPage() {
    return (
        <>
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="공유 파일" />
                <NarrowBox>
                    <CustomIcon type="file" stage="DRAFT" secu="CONFIDENTIAL"/>
                    <CustomIcon type="folder" stage="DRAFT" secu="CONFIDENTIAL"/>
                </NarrowBox>
                <BodyHeader text="내 파일" />
            </BodyFrame>
        </>
    )
}
