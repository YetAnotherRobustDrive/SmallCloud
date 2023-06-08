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
import PostMoveFile from "../../services/file/PostMoveFile";

export default function MainPage() {

    const [isFileView, setIsFileView] = useState(false);
    const [selected, setSelected] = useState();
    const [isLoading, setIsLoading] = useState(true);
    const [target, setTarget] = useState(0);
    const [source, setSource] = useState({ type: "", id: 0 });
    const [gridFiles, setGridFiles] = useState([]);
    const [shareFiles, setShareFiles] = useState([]);

    useEffect(() => {
        const render = async () => {
            const rootIDRes = await GetRootDir();
            if (!rootIDRes[0]) {
                alert(rootIDRes[1])
                return;
            }

            const rootID = rootIDRes[1];

            const subFileRes = await GetSubFileList(rootID);
            if (!subFileRes[0]) {
                alert(subFileRes[1]);
                return;
            }
            const subDirRes = await GetSubDirList(rootID);
            if (!subDirRes[0]) {
                alert(subDirRes[1]);
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
                        type={data.type} />
                })
            )

            const shareFileRes = await GetShareFileList();
            if (!shareFileRes[0]) {
                alert(shareFileRes[1])
                return;
            }
            const shareDirRes = await GetShareFolderList();
            if (!shareDirRes[0]) {
                alert(shareDirRes[1])
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
                        noContext={true} />
                })
            )
        }
        render();
        setTimeout(() => setIsLoading(false), 250);
    }, [])

    useEffect(() => {
        const move = async () => {
            if (source.type === "file") {
                const res = await PostMoveFile(source.id, target);
                if (!res[0]) {
                    alert("오류가 발생했습니다.");
                    return;
                }
            }
            else if (source.type === "folder") {
                const res = await PostMoveDir(source.id, target);
                if (!res[0]) {
                    alert("오류가 발생했습니다.");
                    return;
                }
            }
            setTarget(0);
            setSource({ type: "", id: 0 });
            setGridFiles([
                ...gridFiles.filter((data) => data.props.id !== source.id),
            ])
        }
        if (target !== 0 && source.id !== 0 && target !== source.id) {
            move();
        }
    }, [target, source])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            <Header />
            <Sidebar />
            <BodyFrame hasContext={true}>
                <BodyHeader text="내 파일" isSortable/>
                {
                    gridFiles.length === 0 ? <div style={{ height: "calc(100vh - 299px)", textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> :
                        <GridBox height="calc(100vh - 299px)">
                            {gridFiles}
                        </GridBox>
                }
                <UploadBtn />
                <BodyHeader text="공유받은 파일" />
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
