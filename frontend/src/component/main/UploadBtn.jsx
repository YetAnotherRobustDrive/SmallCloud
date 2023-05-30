import React, { useState } from "react";
import { AiFillPlusCircle } from 'react-icons/ai'
import { GoCloudUpload } from 'react-icons/go'
import { TbDragDrop } from 'react-icons/tb'
import { useParams } from 'react-router-dom'
import '../../css/load.css'
import PostNewFile from "../../services/file/PostNewFile";
import GetRootDir from "../../services/directory/GetRootDir";
import ModalOk from "../modal/ModalOk";

export default function UploadBtn() {

    const [isOpen, setIsOpen] = useState(false);
    const [isHover, setIsHover] = useState(false);
    const [isFail, setIsFail] = useState();
    const [message, setMessage] = useState();
    const [file, setFile] = useState();
    const [filename, setFilename] = useState();
    const params = useParams();

    const handleDrop = (e) => {
        e.preventDefault();
        setIsHover(false);
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
            const res = await PostNewFile(formData);
            console.log(res);
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]);
                return;
            }
        }
        try {
            e.preventDefault();
            render();
        } catch (error) {
            setIsFail(true);
            setMessage(error);
        }
    }

    return (
        <>
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
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
                                onDrop={handleDrop}></input>
                            <span className="title">{"현재 위치 파일 업로드"}</span>
                            <label htmlFor="file" className={isHover ? "bayHover" : "bay"} >
                                {isHover ? <GoCloudUpload /> : <TbDragDrop/>}
                            </label>
                            <span className="subtitle">
                                {filename === undefined ? "Drag & Drop or Click!" : "파일 : " + filename}
                            </span>
                            <button className="upBtn" type="submit">업로드</button>
                        </div>
                    )}
                    <div className="btn" onClick={() => setIsOpen(!isOpen)}><AiFillPlusCircle /></div>
                </form>
            </div>
        </>
    )
}