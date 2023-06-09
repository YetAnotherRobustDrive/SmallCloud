import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import CustomIcon from "../../component/main/CustomIcon";
import GridBox from "../../component/main/GridBox";
import UploadBtn from "../../component/main/UploadBtn";
import ModalFileview from "../../component/modal/ModalFileview";
import ModalLoading from "../../component/modal/ModalLoading";
import Sidebar from "../../component/sidebar/Sidebar";
import GetFavoriteList from "../../services/file/GetFavoriteList";

export default function FavoritesPage() {
    const [isFileView, setIsFileView] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState();
    const [gridFiles, setGridFiles] = useState([]);
    const [sort, setSort] = useState("name_asc");

    useEffect(() => {
        const init = async () => {
            const fileRes = await GetFavoriteList();
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
                        data={data} />
                })
            );
        }
        init();
        setTimeout(() => setIsLoading(false), 250);
    }, [])

    useEffect(() => {
        setGridFiles([...
        gridFiles.sort((a, b) => {
            if (sort === "name_asc") {
                return a.props.data.name.localeCompare(b.props.data.name);
            }
            else if (sort === "name_desc") {
                return b.props.data.name.localeCompare(a.props.data.name);
            }
            else if (sort === "time_asc") {
                return a.props.data.createdDate.localeCompare(b.props.data.createdDate);
            }
            else if (sort === "time_desc") {
                return b.props.data.createdDate.localeCompare(a.props.data.createdDate);
            }
        })])
    }, [sort])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            <Header />
            <Sidebar />
            <BodyFrame>
                <BodyHeader text="Favorites" isSortable setter={setSort}/>
                    <GridBox height="calc(100vh - 117px)">
                        {gridFiles}
                    </GridBox>
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
