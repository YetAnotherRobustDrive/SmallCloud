import React, { useState } from "react";
import { AiFillPlusCircle } from 'react-icons/ai';
import { GoCloudUpload } from 'react-icons/go';
import { TbDragDrop } from 'react-icons/tb';
import { useParams } from 'react-router-dom';
import '../../css/load.css';
import GetConfig from "../../services/config/GetConfig";
import GetRootDir from "../../services/directory/GetRootDir";
import PostNewFile from "../../services/file/PostNewFile";
import GetUserUsage from "../../services/user/GetUserUsage";
import SwalAlert from "../swal/SwalAlert";
import SwalError from "../swal/SwalError";
import SwalLoadingBy from "../swal/SwalLoadingBy";
import ProgressBar from "../updown/ProgressBar";

export default function UploadBtn() {

    const [isOpen, setIsOpen] = useState(false);
    const [isHover, setIsHover] = useState(false);
    const [file, setFile] = useState();
    const [filename, setFilename] = useState();
    const [uploadState, setUploadState] = useState(0);
    const [isOnEncrypt, setIsOnEncrypt] = useState(false);
    const [isOnEncode, setIsOnEncode] = useState(false);

    const params = useParams();


    const handleDrop = (e) => {
        e.preventDefault();
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

        const encrypt = async (isEncryp) => {
            return new Promise((resolve, reject) => {
                if (isEncryp) {
                    setTimeout(() => { //요기에 암호화 코드 넣으면 됨
                        setIsOnEncrypt(false)
                        resolve()
                    }, 10000);
                }
                else
                    resolve();
            });
        }

        const encode = async (isEncode) => {
            return new Promise((resolve, reject) => {
                if (isEncode) {
                    setTimeout(() => { //요기에 인코딩 코드 넣으면 됨
                        setIsOnEncode(false)
                        resolve()
                    }, 10000);
                }
                else
                    resolve();
            });
        }
        const render = async () => {
            if (file === undefined || file === null) {
                SwalError("파일을 선택해주세요.");
                return;
            }

            setIsOnEncode(e.target.isEncode.checked);
            setIsOnEncrypt(e.target.isEncryp.checked);

            await encode(e.target.isEncode.checked);

            await encrypt(e.target.isEncryp.checked);

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
        setFilename(e.target.files[0].name);
        setFile(e.target.files[0])
    }

    return (
        <>
            {isOnEncode && SwalLoadingBy("인코딩 중입니다.")}
            {(isOnEncrypt  && !isOnEncode) && SwalLoadingBy("암호화 중입니다.")}
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
                            <div className="optionBtn">
                                <label><input name="isEncode" type="checkbox" onClick={
                                    (e) => {
                                        if (e.target.checked === true) {
                                            SwalAlert("info", "인코딩은 파일 업로드 시간이 길어질 수 있습니다.");
                                        }
                                    }
                                } />스트리밍 최적화(인코딩)</label>
                                <label><input name="isEncryp" type="checkbox" onClick={
                                    (e) => {
                                        if (e.target.checked === true) {
                                            SwalAlert("info", "암호화는 파일 업로드 시간이 길어질 수 있습니다.");
                                        }
                                    }
                                } />암호화</label>
                            </div>
                            <button className="upBtn" type="submit">업로드</button>
                        </div>
                    )}
                    <div className="btn" onClick={() => setIsOpen(!isOpen)}><AiFillPlusCircle /></div>
                </form>
            </div>
        </>
    )
}