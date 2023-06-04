import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import CustomIcon from "../../component/main/CustomIcon";
import GridBox from "../../component/main/GridBox";
import ListBox from "../../component/main/ListBox";
import UploadBtn from "../../component/main/UploadBtn";
import ModalFileview from "../../component/modal/ModalFileview";
import ModalLoading from "../../component/modal/ModalLoading";
import Sidebar from "../../component/sidebar/Sidebar";

export default function FavoritesPage() {

    const [isGrid, setIsGrid] = useState(true);
    const [isFileView, setIsFileView] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState();

    useEffect(() => {
        setTimeout(() => setIsLoading(false), 500);
    }, [])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="Favorites" addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    <GridBox height="calc(100vh - 117px)">
                        //need fetch
                    </GridBox>
                }
                {!isGrid &&
                    <>
                        <div className="listscroll" style={{ height: "calc(100vh - 117px)" }}>
                            //need fetch                        
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
