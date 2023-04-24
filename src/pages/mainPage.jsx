import React from "react";
import Sidebar from "../component/sidebar/sidebar"
import Header from "../component/header/header"
import BodyHeader from "../component/main/bodyHeader";
import BodyFrame from "../component/bodyframe";
import NarrowBox from "../component/main/narrowBox";
import CustomIcon from "../component/main/customIcon";
import GridBox from "../component/main/gridBox";

import datas from '../fakeJSON/direcFiles.json'

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
            </BodyFrame>
        </>
    )
}
