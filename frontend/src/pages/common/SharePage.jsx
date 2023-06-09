import React, { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import CustomIcon from "../../component/main/CustomIcon";
import GridBox from "../../component/main/GridBox";
import UploadBtn from "../../component/main/UploadBtn";
import ModalFileview from "../../component/modal/ModalFileview";
import ModalLoading from "../../component/modal/ModalLoading";
import Sidebar from "../../component/sidebar/Sidebar";
import PostMoveDir from "../../services/directory/PostMoveDir";
import GetShareFileList from "../../services/share/GetShareFileList";
import GetShareFolderList from "../../services/share/GetShareFolderList";

export default function SharePage() {

    const [isFileView, setIsFileView] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState();
    const [target, setTarget] = useState(0);
    const [source, setSource] = useState(0);
    const [gridFiles, setGridFiles] = useState([]);
    const params = useParams();
    const [sort, setSort] = useState("name_asc");

    useEffect(() => {
        const render = async () => {
            const shareFileRes = await GetShareFileList();
            if (!shareFileRes[0]) {
                alert(shareFileRes[1]);
                return;
            }
            const shareDirRes = await GetShareFolderList();
            if (!shareDirRes[0]) {
                alert(shareDirRes[1]);
                return;
            }
            const files = [...shareDirRes[1], ...shareFileRes[1]];
            setGridFiles(
                files.map((data) => {
                    return <CustomIcon
                        onClick={() => {
                            setSelected(data);
                            setIsFileView(true);
                        }}
                        targetSetter={setTarget}
                        sourceSetter={setSource}
                        key={data.id}
                        data={data}/>
                })
            );
        }
        try {
            setIsLoading(true);
            render();
            setTimeout(() => setIsLoading(false), 250);
        } catch (error) {
            alert(error);
            setIsLoading(false);
        }
    }, [params.fileID])

    useEffect(() => {
        const move = async () => {
            const res = await PostMoveDir(source, target);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
            setTarget(0);
            setSource(0);
            setGridFiles([
                ...gridFiles.filter((data) => {
                    return data.props.data.id !== source.id;
                }),
            ]);
        }
        if (target !== 0 && source !== 0 && target !== source) {
            move();
        }
    }, [target, source])

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
            <BodyFrame hasContext={true}>
                <BodyHeader text={"공유받은 파일"} isSortable setter={setSort}/>
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
