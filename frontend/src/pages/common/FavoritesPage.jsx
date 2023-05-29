import React, { useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import CustomIcon from "../../component/main/CustomIcon";
import GridBox from "../../component/main/GridBox";
import ListBox from "../../component/main/ListBox";
import UploadBtn from "../../component/main/UploadBtn";
import ModalFileview from "../../component/modal/ModalFileview";
import Sidebar from "../../component/sidebar/Sidebar";

import datas from '../../fakeJSON/direcFiles.json';

export default function FavoritesPage() {

    const [isGrid, setIsGrid] = useState(true);
    const [isFileView, setIsFileView] = useState(false);
    const [selected, setSelected] = useState();

    return (
        <>
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="Favorites" addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    <GridBox height="calc(100vh - 117px)">
                        {
                            datas.map((data) => {
                                return <CustomIcon 
                                onClick={() => {
                                    setSelected(data);
                                    setIsFileView(true);
                                }}
                                key={data.id}
                                id={data.id}
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
                        <div className="listscroll" style={{ height: "calc(100vh - 117px)" }}>{
                            datas.map((data) => {
                                return <ListBox  
                                key={data.id} 
                                onClick={() => {
                                    setSelected(data);
                                    setIsFileView(true);
                                }}
                                data={data} />
                            })
                        }
                        </div>
                    </>
                }
                <UploadBtn />
            </BodyFrame>
            {isFileView && (
                <>
                    <ModalFileview
                        file={selected}
                        after={() => setIsFileView(false)} />
                </>
            )
            }
        </>
    )
}
