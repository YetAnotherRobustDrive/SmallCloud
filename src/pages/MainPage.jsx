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
import ModalFileview from "../component/modal/ModalFileview";

export default function MainPage() {

    const [isGrid, setIsGrid] = useState(true);
    const [isFileOpen, setIsFileOpen] = useState(true);
    const [selected, setSelected] = useState();

    return (
        <>
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="공유 파일" />
                <NarrowBox>
                    {
                        datas.map((data) => {
                            return <CustomIcon
                                key={data.id}
                                name={data.name}
                                type={data.type}
                                stage={data.writingStage}
                                secu={data.securityLevel} />
                        })
                    }
                </NarrowBox>
                <BodyHeader text="내 파일" addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    <GridBox height="calc(100vh - 299px)">
                        {
                            datas.map((data) => {
                                return <CustomIcon
                                    key={data.id}
                                    name={data.name}
                                    type={data.type}
                                    stage={data.writingStage}
                                    secu={data.securityLevel} />
                            })
                        }
                    </GridBox>
                }
                {!isGrid &&
                    <>
                        <div className="listscroll" style={{ height: "calc(100vh - 299px)" }}>{
                            datas.map((data) => {
                                setSelected(data);
                                return <ListBox key={data.id} data={data} />
                            })
                        }
                        </div>
                    </>
                }
                <UploadBtn />
            </BodyFrame>
            {isFileOpen &&
                <ModalFileview 
                file={selected} 
                after={() => setIsFileOpen(false)}/>
            }
        </>
    )
}
