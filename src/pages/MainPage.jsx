import React, { useState } from "react";
import Sidebar from "../component/sidebar/Sidebar"
import Header from "../component/header/Header"
import BodyHeader from "../component/main/BodyHeader";
import BodyFrame from "../component/Bodyframe";
import NarrowBox from "../component/main/NarrowBox";
import CustomIcon from "../component/main/CustomIcon";
import GridBox from "../component/main/GridBox";
import UploadBtn from "../component/main/UploadBtn";
import ListBox from "../component/main/ListBox";

import datas from '../fakeJSON/direcFiles.json'

export default function MainPage() {

    const [isGrid, setIsGrid] = useState(true);

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
                <BodyHeader text="내 파일" addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    <GridBox>
                        {
                            datas.map((data) => {
                                return <CustomIcon type={data.type} stage={data.writingStage} secu={data.securityLevel} />
                            })
                        }
                    </GridBox>
                }
                {!isGrid &&
                    <>
                    <div className="listTitle">제목</div>
                    <div className="listscroll">{
                        datas.map((data) => {
                            return <ListBox data={data} />
                        })
                    }
                    </div>
                    </>
                }
                <UploadBtn />
            </BodyFrame>
        </>
    )
}
