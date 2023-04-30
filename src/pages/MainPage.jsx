import React from "react";
import Sidebar from "../component/sidebar/Sidebar"
import Header from "../component/header/Header"
import BodyHeader from "../component/main/BodyHeader";
import BodyFrame from "../component/Bodyframe";
import NarrowBox from "../component/main/NarrowBox";
import CustomIcon from "../component/main/CustomIcon";
import GridBox from "../component/main/GridBox";

import datas from '../fakeJSON/direcFiles.json'
import UploadBtn from "../component/main/UploadBtn";

export default function MainPage() {
    return (
        <>
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="공유 파일" />
                <NarrowBox>
                    {
                        datas.map((data) => {
                            return <CustomIcon type={data.type} stage={data.writingStage} secu={data.securityLevel} />
                        })
                    }
                </NarrowBox>
                <BodyHeader text="내 파일" addon="true"/>
                <GridBox>
                    {
                        datas.map((data) => {
                            return <CustomIcon type={data.type} stage={data.writingStage} secu={data.securityLevel} />
                        })
                    }
                </GridBox>
                <UploadBtn/>
            </BodyFrame>
        </>
    )
}
