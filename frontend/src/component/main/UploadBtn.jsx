import React, { useState } from "react";
import { AiFillPlusCircle } from 'react-icons/ai'
import { GoCloudUpload } from 'react-icons/go'
import { TbDragDrop } from 'react-icons/tb'
import { useParams } from 'react-router-dom'
import '../../css/load.css'
import PostNewFile from "../../services/file/PostNewFile";
import GetRootDir from "../../services/directory/GetRootDir";
import ModalOk from "../modal/ModalOk";
import ProgressBar from "../updown/ProgressBar";

export default function UploadBtn() {

    const [isOpen, setIsOpen] = useState(false);
    const [isHover, setIsHover] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [isEnd, setIsEnd] = useState(false);
    const [message, setMessage] = useState();
    const [file, setFile] = useState();
    const [filename, setFilename] = useState();
    const [uploadState, setUploadState] = useState(0);
    const params = useParams();

    const handleDrop = (e) => {
        e.preventDefault();
        setUploadState(0);
        setIsEnd(false);
        setIsFail(false);
        setIsHover(false);
        if (uploadState !== 0) {
            setIsFail(true);
            setMessage("현재 업로드 진행 중입니다.");
            return;
        }
        if (e.dataTransfer.files[0].type === "") {
            setIsFail(true);
            setMessage("업로드할 수 없는 형식의 파일입니다.");
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
        const render = async () => {
            const formData = new FormData();
            let curr = params.fileID;
            if (curr === undefined) {
                const rootIDRes = await GetRootDir();
                if (!rootIDRes[0]) {
                    setIsFail(true);
                    setMessage(rootIDRes[1]);
                    return;
                }
                curr = rootIDRes[1];
            }
            formData.append('cwd', curr);
            formData.append('file', file);
            const res = await PostNewFile(formData, setUploadState, setIsEnd);
            console.log(res);
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]);
                return;
            }
        }
        try {
            e.preventDefault();
            if (uploadState !== 0) {
                setIsFail(true);
                setMessage("현재 업로드 진행 중입니다.");
                return;
            }
            render();
        } catch (error) {
            setIsFail(true);
            setMessage(error);
        }
    }

    const handleChange = (e) => {
        setFilename(e.target.files[0].name);
        setFile(e.target.files[0])
    }

    return (
        <>
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isEnd && <ModalOk close={() => window.location.reload()}>{"업로드 완료"}</ModalOk>}
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