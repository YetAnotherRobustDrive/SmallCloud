import React, { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
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
import GetSubDirList from "../../services/directory/GetSubDirList";
import GetSubFileList from "../../services/directory/GetSubFileList";
import GetDirInfo from "../../services/directory/GetDirInfo";
import PostMoveDir from "../../services/directory/PostMoveDir";

export default function FolderPage() {

    const [isGrid, setIsGrid] = useState(true);
    const [isFileView, setIsFileView] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState();
    const [isFail, setIsFail] = useState();
    const [message, setMessage] = useState();
    const [name, setName] = useState([]);
    const [target, setTarget] = useState(0);
    const [source, setSource] = useState(0);
    const [gridFiles, setGridFiles] = useState([]);
    const [listFiles, setListFiles] = useState([]);
    const params = useParams();

    useEffect(() => {
        const render = async () => {
            const infoRes = await GetDirInfo(params.fileID);
            if (!infoRes[0]) {
                throw infoRes[1];
            }
            setName(infoRes[1].name);
            const subFileRes = await GetSubFileList(params.fileID);
            if (!subFileRes[0]) {
                throw subFileRes[1];
            }
            const subDirRes = await GetSubDirList(params.fileID);
            if (!subDirRes[0]) {
                throw subDirRes[1];
            }
            const files = [...subDirRes[1], ...subFileRes[1]];
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
                        id={data.id}
                        name={data.name}
                        type={data.type}
                        stage={data.writingStage}
                        secu={data.securityLevel} />
                })
            );
            setListFiles(
                files.map((data) => {
                    return <ListBox
                        key={data.id}
                        onClick={() => {
                            setSelected(data);
                            setIsFileView(true);
                        }}
                        data={data} />
                })
            );
        }
        try {
            setIsLoading(true);
            render();
            setTimeout(() => setIsLoading(false), 500);
        } catch (error) {
            setIsFail(true);
            setMessage(error);
            setIsLoading(false);
        }
    }, [params.fileID])

    useEffect(() => {
        const move = async () => {
            const res = await PostMoveDir(source, target);
            setTarget(0);
            setSource(0);
            window.location.reload();
        }
        if (target !== 0 && source !== 0 && target !== source) {
            move();
        }
    }, [target, source])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            <Header />
            <Sidebar />
            <BodyFrame hasContext={true}>
                <BodyHeader text={name} addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    (gridFiles.length === 0 ? <div style={{textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> :
                        <GridBox height="calc(100vh - 117px)">
                            {gridFiles}
                        </GridBox>)
                }
                {!isGrid &&
                    <div className="listscroll" style={{ height: "calc(100vh - 299px)" }}>
                        {listFiles.length === 0 ? <div style={{ textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> : listFiles}
                    </div>
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
