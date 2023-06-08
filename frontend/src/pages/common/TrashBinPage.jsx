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
import GetTrashFileList from "../../services/file/GetTrashFileList";

export default function TrashBinPage() {

    const [isGrid, setIsGrid] = useState(true);
    const [isFileView, setIsFileView] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState();
    const [gridFiles, setGridFiles] = useState([]);
    const [listFiles, setListFiles] = useState([]);

    useEffect(() => {
        const init = async () => {
            const fileRes = await GetTrashFileList();
            if (!fileRes[0]) {
                alert(fileRes[1]);
                return;
            }
            // const folderRes = await GetTrashFolderList();
            // if (!folderRes[0]) {
            //     alert(folderRes[1]);
            //     return;
            // }
            //const files = [...folderRes[1], ...fileRes[1]];
            const files = [...fileRes[1]];
            setGridFiles(
                files.map((data) => {
                    return <CustomIcon
                        onClick={() => {
                            setSelected(data);
                            setIsFileView(true);
                        }}
                        key={data.id}
                        id={data.id}
                        name={data.name}
                        type={data.type} />
                })
            );
            setListFiles(
                files.map((data) => {
                    return <ListBox key={data.id}
                        onClick={() => {
                            setSelected(data);
                            setIsFileView(true);
                        }}
                        data={data} />
                })
            );
        }
        init();
        setTimeout(() => setIsLoading(false), 250);
    }, []);


    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="Trash" addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    <GridBox height="calc(100vh - 117px)">
                        {gridFiles}
                    </GridBox>
                }
                {!isGrid &&
                    <>
                        <div className="listscroll" style={{ height: "calc(100vh - 117px)" }}>
                            {listFiles}
                        </div>
                    </>
                }
                <UploadBtn />
            </BodyFrame>
            {isFileView && (
                <>
                    <ModalFileview
                        isDeleted={true}
                        file={selected}
                        after={() => setIsFileView(false)} />
                </>
            )
            }
        </>
    )
}
