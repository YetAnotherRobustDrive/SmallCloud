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

export default function Trash() {

    const [isGrid, setIsGrid] = useState(true);

    return (
        <>
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="Trash" addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    <GridBox height="calc(100vh - 117px)">
                        {
                            datas.map((data) => {
                                return <CustomIcon type={data.type} stage={data.writingStage} secu={data.securityLevel} />
                            })
                        }
                    </GridBox>
                }
                {!isGrid &&
                    <>
                        <div className="listscroll" style={{ height: "calc(100vh - 117px)" }}>{
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
