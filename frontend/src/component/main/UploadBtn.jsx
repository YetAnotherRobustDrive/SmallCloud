import React, { useEffect, useState } from "react";
import { AiFillPlusCircle } from 'react-icons/ai';
import { GoCloudUpload } from 'react-icons/go';
import { TbDragDrop } from 'react-icons/tb';
import { useParams } from 'react-router-dom';
import '../../css/load.css';
import GetConfig from "../../services/config/GetConfig";
import GetRootDir from "../../services/directory/GetRootDir";
import PostNewFile from "../../services/file/PostNewFile";
import PostNewMpd from "../../services/file/PostNewMpd";
import PostNewSegments from "../../services/file/PostNewSegments";
import GetUserUsage from "../../services/user/GetUserUsage";
import SwalAlert from "../swal/SwalAlert";
import SwalError from "../swal/SwalError";
import ProgressBar from "../updown/ProgressBar";

export default function UploadBtn() {

    const [isOpen, setIsOpen] = useState(false);
    const [isHover, setIsHover] = useState(false);
    const [file, setFile] = useState();
    const [filename, setFilename] = useState();
    const [uploadState, setUploadState] = useState(0)

    const params = useParams();

    const handleDrop = (e) => {
        e.preventDefault();
        setIsHover(false);
        if (uploadState !== 0) {
            SwalError("현재 업로드 진행 중입니다.");
            return;
        }
        if (e.dataTransfer.files[0].type === "" || e.dataTransfer.files[0].type === "mpd") {
            SwalError("지원하지 않는 파일 형식입니다.");
            return;
        }
        const fileInput = window.document.querySelector('input[type="file"]');
        const data = new DataTransfer();
        data.items.add(e.dataTransfer.files[0]);
        setFilename(e.dataTransfer.files[0].name);
        setFile(e.dataTransfer.files[0]);
        fileInput.files = data.files;
    }

    const handleUpload = (e) => {


        const checkUsage = async () => {
            const configRes = await GetConfig("301");
            if (!configRes[0]) {
                throw new Error("서버 연결에 실패했습니다.");
            }
            const maxSize = parseInt(configRes[1]) * 1000 * 1000 * 1000;
            const usage = await GetUserUsage();
            if (!usage[0]) {
                throw new Error("서버 연결에 실패했습니다.");
            }

            const fileSize = parseInt(file.size);
            const used = parseInt(usage[4]);
            if (maxSize < fileSize + used) {
                const size = maxSize - used;
                let converted;
                if (parseInt(size / Math.pow(10, 9)) > 0) {
                    converted = (size / Math.pow(10, 9)).toFixed(1) + "GB";
                }
                else if (parseInt(size / Math.pow(10, 6)) > 0) {
                    converted = (size / Math.pow(10, 6)).toFixed(1) + "MB";
                }
                else if (parseInt(size / Math.pow(10, 3)) > 0) {
                    converted = (size / Math.pow(10, 3)).toFixed(1) + "KB";
                }
                else {
                    converted = size + "B";
                }
                throw new Error("사용 가능한 용량을 초과하였습니다. 잔여 용량 : " + converted);
            }
            return true;
        }

        const getFolder = async () => {
            let curr = params.fileID;
            if (curr === undefined) {
                const rootIDRes = await GetRootDir();
                if (!rootIDRes[0]) {
                    SwalError(rootIDRes[1]);
                    throw new Error("폴더를 불러오는데 실패하였습니다.");
                }
                curr = rootIDRes[1];
            }
            return curr;
        }

        const uploadSingle = async () => {
            const curr = await getFolder();
            const formData = new FormData();
            formData.append('cwd', curr);
            formData.append('file', file);
            const res = await PostNewFile(formData, setUploadState, () => SwalAlert("success", "업로드가 완료되었습니다.", () => window.location.reload()));
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
        }

        const uploadEncoded = async (encoded) => {
            const formDataOrigin = new FormData();
            const curr = await getFolder();
            formDataOrigin.append('cwd', curr);
            formDataOrigin.append('file', file);
            const uploadOrigin = await PostNewFile(formDataOrigin, () => { }, () => { });
            if (!uploadOrigin[0]) {
                SwalError(uploadOrigin[1]);
                return;
            }
            const originId = uploadOrigin[1].id;
            const mpdRaw = await window.electron.getFromLocal(encoded.mpdPath);
            const mpdFile = new File(
                [mpdRaw],
                encoded.mpdPath.includes("/") ?
                    encoded.mpdPath.split("/")[encoded.mpdPath.split("/").length - 1] :
                    encoded.mpdPath.split("\\")[encoded.mpdPath.split("\\").length - 1],
            )
            const formData = new FormData();
            formData.append('originFileId', originId);
            formData.append('file', mpdFile);
            const res = await PostNewMpd(formData);
            const segment = encoded.files;
            for (let i = 0; i < segment.length; i++) {
                if (segment[i] === undefined) {
                    throw new Error("인코딩에 실패하였습니다.");
                }
                const segRaw = await window.electron.getFromLocal("data/" + segment[i]);
                const segFile = new File(
                    [segRaw],
                    segment[i],
                )
                const formData = new FormData();
                formData.append('file', segFile);
                const res2 = await PostNewSegments(formData, res[1].id);
                if (!res2[0]) {
                    SwalError(res2[1]);
                    return;
                }
            }
            //dir clear
            SwalAlert("success", "업로드가 완료되었습니다.", () => window.location.reload());
        }

        const render = async () => {
            if (file === undefined || file === null) {
                SwalError("파일을 선택해주세요.");
                return;
            }
            checkUsage();
            uploadSingle();

        }
        try {
            e.preventDefault();
            if (uploadState !== 0) {
                SwalError("현재 업로드 진행 중입니다.");
                return;
            }
            if (file === undefined) {
                SwalError("파일을 선택해주세요.");
                return;
            }
            render();
        } catch (error) {
            SwalError(error);
        }
    }

    const handleChange = (e) => {
        if (e.target.files.length === 0) {
            return;
        }
        if (e.target.files[0].type === "" || e.target.files[0].type === "mpd") {
            SwalError("지원하지 않는 파일 형식입니다.");
            return;
        }
        setFilename(e.target.files[0].name);
        setFile(e.target.files[0])
    }

    return (
        <>
            <div className="upload-btn">
                <form className="btn-header" onSubmit={handleUpload}>
                    {isOpen && (
                        <div className="open-space">
                            <input className="droparea"
                                type="file"
                                name="location"
                                id="file"
                                onDragEnter={() => setIsHover(true)}
                                onDragLeave={() => setIsHover(false)}
                                onDrop={handleDrop}
                                onChange={handleChange}></input>
                            <span className="title">{"현재 위치 파일 업로드"}</span>
                            <label htmlFor="file" className={isHover ? "bayHover" : "bay"} >
                                {isHover ? <GoCloudUpload /> : <TbDragDrop />}
                            </label>
                            <span className="subtitle">
                                {filename === undefined ? "Drag & Drop or Click!" : "파일 : " + filename}
                            </span>
                            <ProgressBar value={uploadState} />
                            <button className="upBtn" type="submit">업로드</button>
                        </div>
                    )}
                    <div className="btn" onClick={() => setIsOpen(!isOpen)}><AiFillPlusCircle /></div>
                </form>
            </div>
        </>
    )
}