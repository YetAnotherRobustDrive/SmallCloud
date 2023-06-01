import React, { useState } from "react";
import '../../css/fileview.css';
import ModalFileopen from "./ModalFileopen";
import { AiOutlineClose } from "react-icons/ai";
import { MdOpenInFull } from "react-icons/md";
import { GoCloudDownload } from 'react-icons/go'
import { TbEdit } from 'react-icons/tb'
import GetDownloadFile from "../../services/file/GetDownloadFile";
import ProgressBar from "../../component/updown/ProgressBar"
import ModalEmpty from "./ModalEmpty";

export default function ModalFileview(props) {
    const [isFileOpen, setIsFileOpen] = useState(false);
    const [isGeneralSelected, setIsGeneralSelected] = useState(true);
    const [isNowDownload, setIsNowDownload] = useState(false);
    const [percentage, setPercentage] = useState(0);
    const fileData = props.file;

    const handleDownload = async (e) => {
        setIsNowDownload(true);
        const res = await GetDownloadFile(fileData.id, setPercentage, () => { }, fileData.name)
        setTimeout(() => setIsNowDownload(false), 500);
    }

    const handleLabelEdit = (e) => {
        e.stopPropagation();
        e.preventDefault();
    }

    return (
        <>
            {!isNowDownload &&
                <div className="fileview" >
                    <div className='head'>
                        <span className='filename'>{fileData.name}</span>
                        <div className="fileBtn">
                            <div className='icon' onClick={handleDownload}><GoCloudDownload /></div>
                            <div className='icon' onClick={() => setIsFileOpen(true)}><MdOpenInFull /></div>
                            <div className='icon' onClick={() => props.after()}><AiOutlineClose /></div>
                        </div>
                    </div>
                    <div className="body">
                        <div className="options">
                            <span
                                style={isGeneralSelected ? { textDecoration: "underline" } : {}}
                                onClick={() => setIsGeneralSelected(true)}>
                                일반
                            </span>
                            <span
                                style={!isGeneralSelected ? { textDecoration: "underline" } : {}}
                                onClick={() => setIsGeneralSelected(false)}>
                                공유
                            </span>
                        </div>
                        {isGeneralSelected &&
                            <>
                                <div className="labels">
                                    <span>라벨</span>
                                    <button onClick={handleLabelEdit} className="icon" ><TbEdit /></button>
                                    <div className="label">
                                        {fileData.labels.length === 0? "없음" : fileData.labels.map((label, index) => {
                                            return ("#" + label + " ")
                                        })}
                                    </div>
                                </div>
                                <div className="column">
                                    <span>크기</span>
                                    <div className="value">
                                        {fileData.size}
                                    </div>
                                </div>
                                <div className="column">
                                    <span>파일 형식</span>
                                    <div className="value">
                                        {fileData.name.substring(fileData.name.lastIndexOf(".") + 1, fileData.name.length)}
                                        </div>
                                </div>
                                <div className="column">
                                    <span>작성자</span>
                                    <div className="value">
                                        {fileData.authorName}
                                        </div>
                                </div>
                                <div className="column">
                                    <span>생성일</span>
                                    <div className="value">
                                    {fileData.createdDate.substring(0, fileData.createdDate.indexOf("T")).replace(/-/g, ".")}
                                    </div>
                                </div>
                            </>
                        }
                        {!isGeneralSelected &&
                            <>
                                <div>{fileData.shared ? "공유 O" : "공유 X"}</div>
                            </>
                        }
                    </div>
                    {isFileOpen &&
                        <ModalFileopen
                            after={() => setIsFileOpen(false)}
                        />
                    }
                </div>}
            {isNowDownload &&
                <ModalEmpty close={() => setPercentage(0)} isOpen={isNowDownload}>
                    <>
                        <span className="customSpanForDown">{fileData.name + "\n다운로드 중..."} </span>
                        <ProgressBar value={percentage} />
                    </>
                </ModalEmpty>
            }
        </>
    )

}