import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import CustomIcon from "../../component/main/CustomIcon";
import GridBox from "../../component/main/GridBox";
import UploadBtn from "../../component/main/UploadBtn";
import ModalFileview from "../../component/modal/ModalFileview";
import ModalLoading from "../../component/modal/ModalLoading";
import Sidebar from "../../component/sidebar/Sidebar";
import GetDirInfo from "../../services/directory/GetDirInfo";
import GetSubDirList from "../../services/directory/GetSubDirList";
import GetSubFileList from "../../services/directory/GetSubFileList";
import PostMoveDir from "../../services/directory/PostMoveDir";
import PostMoveFile from "../../services/file/PostMoveFile";

export default function FolderPage() {

    const [isFileView, setIsFileView] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [selected, setSelected] = useState();
    const [name, setName] = useState([]);
    const [target, setTarget] = useState(0);
    const [source, setSource] = useState({ type: "", id: 0 });
    const [gridFiles, setGridFiles] = useState([]);
    const [sort, setSort] = useState("name_asc");
    const [parentRes, setParentRes] = useState({});
    const params = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const render = async () => {
            const infoRes = await GetDirInfo(params.fileID);
            if (!infoRes[0]) {
                throw infoRes[1];
            }
            if (infoRes[1].name === "_ROOT_") {
                navigate("/");
                return;
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

            const parentRes = await GetDirInfo(infoRes[1].parentFolderId);
            if (!parentRes[0]) {
                throw parentRes[1];
            }
            setParentRes(parentRes[1]);

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
                        data={data} />
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
                ...gridFiles.filter((data) => data.props.data.id !== source.id),
            ])
        }
        if (target !== 0 && source.id !== 0 && target !== source.id) {
            move();
        }
    }, [target, source])

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
                <BodyHeader text={name} isSortable setter={setSort} />
                {
                    gridFiles.length === 0 ? <div style={{ height: "calc(100vh - 137px)", textAlign: "center", marginTop: "20px" }}>파일이 없습니다.</div> :
                        <GridBox height="calc(100vh - 117px)">
                            <CustomIcon
                                targetSetter={setTarget}
                                sourceSetter={setSource}
                                key={parentRes.id}
                                data={
                                    {
                                        id: parentRes.id,
                                        name: "...",
                                        type: "folder",
                                    }
                                }
                                noContext={true}
                            />
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
