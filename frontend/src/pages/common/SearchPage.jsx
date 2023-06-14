import React, { useEffect, useState } from "react";
import { useSearchParams } from 'react-router-dom';
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import CustomIcon from "../../component/main/CustomIcon";
import GridBox from "../../component/main/GridBox";
import UploadBtn from "../../component/main/UploadBtn";
import ModalFileview from "../../component/modal/ModalFileview";
import ModalLoading from "../../component/modal/ModalLoading";
import Sidebar from "../../component/sidebar/Sidebar";
import GetSearchFolder from "../../services/directory/GetSearchFolder";
import GetSearchFile from "../../services/file/GetSearchFile";
import GetLabelSearch from "../../services/label/GetLabelSearch";

export default function SearchPage() {

    const [isFileView, setIsFileView] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState();
    const [gridFiles, setGridFiles] = useState([]);
    const [sort, setSort] = useState("name_asc");
    const params = useSearchParams();
    const keywords = params[0].get("q").split(" ");

    useEffect(() => {
        const render = async () => {
            keywords.forEach(async (keyword) => {
                if (keyword.startsWith("#")) {
                    const searchLabelRes = await GetLabelSearch(keyword.substring(1));
                    if (searchLabelRes[0] === true) {
                        setGridFiles(
                            searchLabelRes[1].map((e, i) => {
                                return <CustomIcon
                                    onClick={() => {
                                        setSelected(e);
                                        setIsFileView(true);
                                    }}
                                    key={e.id}
                                    data={e} />
                            })
                        )
                    }
                }
                else {
                    const searchFileRes = await GetSearchFile(keyword);
                    const searchFolderRes = await GetSearchFolder(keyword);
                    if (searchFileRes[0] === true && searchFolderRes[0] === true) {
                        const searchRes = [...searchFileRes[1], ...searchFolderRes[1]];
                        setGridFiles(
                            searchRes.map((e, i) => {
                                return <CustomIcon
                                    onClick={() => {
                                        setSelected(e);
                                        setIsFileView(true);
                                    }}
                                    key={e.id}
                                    data={e} />
                            }
                            )
                        )
                    }
                }
            })
        }
        render();
        setIsLoading(false);
    }, [keywords])


    useEffect(() => {
        setGridFiles([
            ...gridFiles.sort((a, b) => {
                if (sort === "name_asc") {
                    return a.props.data.name.localeCompare(b.props.data.name);
                }
                else if (sort === "name_desc") {
                    return b.props.data.name.localeCompare(a.props.data.name);
                }
                else if (sort === "time_asc") {
                    return a.props.data.createdDate.localeCompare(b.props.data.createdDate);
                }
                else {
                    return b.props.data.createdDate.localeCompare(a.props.data.createdDate);
                }
            })])
    }, [sort])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            <Header />
            <Sidebar />
            <BodyFrame hasContext={true}>
                <BodyHeader text={"검색결과"} isSortable setter={setSort} />
                {
                    gridFiles.length === 0 ? <div style={{ height: "calc(100vh - 137px)", textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> :
                        <GridBox height="calc(100vh - 117px)">
                            {gridFiles}
                        </GridBox>
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
