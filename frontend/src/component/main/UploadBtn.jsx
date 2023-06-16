import React, { useState } from "react";
import { AiFillPlusCircle } from 'react-icons/ai';
import { GoCloudUpload } from 'react-icons/go';
import { TbDragDrop } from 'react-icons/tb';
import { useParams } from 'react-router-dom';
import '../../css/load.css';
import GetRootDir from "../../services/directory/GetRootDir";
import PostNewFile from "../../services/file/PostNewFile";
import SwalError from "../swal/SwalError";
import ProgressBar from "../updown/ProgressBar";
import SwalAlert from "../swal/SwalAlert";
import GetUserUsage from "../../services/user/GetUserUsage";
import GetConfig from "../../services/config/GetConfig";

export default function UploadBtn() {

    const [isOpen, setIsOpen] = useState(false);
    const [isHover, setIsHover] = useState(false);
    const [file, setFile] = useState();
    const [filename, setFilename] = useState();
    const [uploadState, setUploadState] = useState(0);
    const params = useParams();

    const handleDrop = (e) => {
        e.preventDefault();
        setUploadState(0);
        setIsHover(false);
        if (uploadState !== 0) {
            SwalError("현재 업로드 진행 중입니다.");
            return;
        }
        if (e.dataTransfer.files[0].type === "") {
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
        const render = async () => {
            const formData = new FormData();
            let curr = params.fileID;
            if (curr === undefined) {
                const rootIDRes = await GetRootDir();
                if (!rootIDRes[0]) {
                    SwalError(rootIDRes[1]);
                    return;
                }
                curr = rootIDRes[1];
            }
            formData.append('cwd', curr);
            formData.append('file', file);

            const configRes = await GetConfig("301");
            if (!configRes[0]) {
                SwalError(configRes[1]);
                return;
            }
            const maxSize = parseInt(configRes[1]) * 1000 * 1000 * 1000;
            const usage = await GetUserUsage();
            if (!usage[0]) {
                SwalError(usage[1]);
                return;
            }
            const fileSize = parseInt(file.size);
            const used = parseInt(usage[4]);
            console.log(fileSize + used);
            console.log(maxSize);
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
                SwalError("사용 가능한 용량을 초과하였습니다. 잔여 용량 : " + converted);
                return;
            }

            const res = await PostNewFile(formData, setUploadState, () => SwalAlert("success", "업로드가 완료되었습니다.", () => window.location.reload()));
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
        }
        try {
            e.preventDefault();
            if (uploadState !== 0) {
                SwalError("현재 업로드 진행 중입니다.");
                return;
            }
            render();
        } catch (error) {
            SwalError(error);
        }
    }

    const handleChange = (e) => {
        setFilename(e.target.files[0].name);
        setFile(e.target.files[0])
    }

    return (
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
    )
}