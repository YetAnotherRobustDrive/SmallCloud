import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import CustomIcon from "../../component/main/CustomIcon";
import GridBox from "../../component/main/GridBox";
import ListBox from "../../component/main/ListBox";
import NarrowBox from "../../component/main/NarrowBox";
import UploadBtn from "../../component/main/UploadBtn";
import ModalFileview from "../../component/modal/ModalFileview";
import ModalLoading from "../../component/modal/ModalLoading";
import Sidebar from "../../component/sidebar/Sidebar";
import GetRootDir from "../../services/directory/GetRootDir";
import GetSubDirList from "../../services/directory/GetSubDirList";
import GetSubFileList from "../../services/directory/GetSubFileList";
import PostMoveDir from "../../services/directory/PostMoveDir";
import GetShareFolderList from "../../services/share/GetShareFolderList";
import GetShareFileList from "../../services/share/GetShareFileList";

export default function MainPage() {

    const [isGrid, setIsGrid] = useState(true);
    const [isFileView, setIsFileView] = useState(false);
    const [selected, setSelected] = useState();
    const [isFail, setIsFail] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [message, setMessage] = useState();
    const [target, setTarget] = useState(0);
    const [source, setSource] = useState(0);
    const [gridFiles, setGridFiles] = useState([]);
    const [listFiles, setListFiles] = useState([]);
    const [shareFiles, setShareFiles] = useState([]);

    useEffect(() => {
        const render = async () => {
            const rootIDRes = await GetRootDir();
            if (!rootIDRes[0]) {
                setIsFail(true);
                setMessage(rootIDRes[1]);
                return;
            }

            const rootID = rootIDRes[1];

            const subFileRes = await GetSubFileList(rootID);
            if (!subFileRes[0]) {
                setIsFail(true);
                setMessage(subFileRes[1]);
                return;
            }
            const subDirRes = await GetSubDirList(rootID);
            if (!subDirRes[0]) {
                setIsFail(true);
                setMessage(subDirRes[1]);
                return;
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
            )
            setListFiles(
                files.map((data) => {
                    return <ListBox key={data.id}
                        onClick={() => {
                            setSelected(data);
                            setIsFileView(true);
                        }}
                        data={data} />
                })
            )

            const shareFileRes = await GetShareFileList();
            if (!shareFileRes[0]) {
                setIsFail(true);
                setMessage(shareFileRes[1]);
                return;
            }
            const shareDirRes = await GetShareFolderList();
            if (!shareDirRes[0]) {
                setIsFail(true);
                setMessage(shareDirRes[1]);
                return;
            }
            const shareFiles = [...shareDirRes[1], ...shareFileRes[1]];
            setShareFiles(
                shareFiles.map((data) => {
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
            )
        }
        render();
        setTimeout(() => setIsLoading(false), 250);
    }, [])

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
                <BodyHeader text="내 파일" addon={setIsGrid} view={isGrid} />
                {isGrid &&
                    (gridFiles.length === 0 ? <div style={{ height: "calc(100vh - 299px)", textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> :
                        <GridBox height="calc(100vh - 299px)">
                            {gridFiles}
                        </GridBox>)
                }
                {!isGrid &&
                    <>
                        <div className="listscroll" style={{ height: "calc(100vh - 299px)" }}>
                            {listFiles.length === 0 ? <div style={{ textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> : listFiles}
                        </div>
                    </>
                }
                <UploadBtn />

                <BodyHeader text="공유 파일" />
                {shareFiles.length === 0 ? <div style={{ textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> :
                    <NarrowBox>
                        {shareFiles}
                    </NarrowBox>
                }
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
