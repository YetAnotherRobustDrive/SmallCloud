import React, { useState } from "react";
import '../../css/fileview.css';
import ModalFileopen from "./ModalFileopen";
import { AiOutlineClose } from "react-icons/ai";
import { MdOpenInFull } from "react-icons/md";
import { GoCloudDownload } from 'react-icons/go'
import {BsFillShareFill} from 'react-icons/bs'
import GetDownloadFile from "../../services/file/GetDownloadFile";
import ProgressBar from "../../component/updown/ProgressBar"
import ModalEmpty from "./ModalEmpty";
import ModalAddShare from "./ModalAddShare";

export default function ModalFileview(props) {
    const [isFileOpen, setIsFileOpen] = useState(false);
    const [isShareOpen, setIsShareOpen] = useState(true);
    const [isGeneralSelected, setIsGeneralSelected] = useState(true);
    const [isNowDownload, setIsNowDownload] = useState(false);
    const [percentage, setPercentage] = useState(0);
    const [sharedList, setSharedList] = useState([]);
    const fileData = props.file;

    const handleDownload = async (e) => {
        setIsNowDownload(true);
        const res = await GetDownloadFile(fileData.id, setPercentage, () => { }, fileData.name)
        setTimeout(() => setIsNowDownload(false), 500);
    }

    const handleShare = async (e) => {
        setIsShareOpen(true);
    }

    return (
        <>
            {!isNowDownload &&
                <div className="fileview" >
                    <div className='head'>
                        <span className='filename'>{fileData.name}</span>
                        <div className="fileBtn">
                            <div className='icon' onClick={handleShare}><BsFillShareFill /></div>
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
                        {isGeneralSelected && //일반
                            <>
                                <div>{fileData.id}</div>
                                <div>{fileData.securityLevel}</div>
                                <div>{fileData.writingStage}</div>
                                <div>{fileData.size}</div>
                            </>
                        }
                        {!isGeneralSelected && //공유
                            <>
                                <div>
                                    <span>현재 공유대상</span>
                                    <div className="shareList">
                                        {sharedList.length === 0 ?
                                            "현재 공유대상이 없습니다." :
                                            sharedList.map((item, index) => { })
                                        }
                                    </div>
                                </div>
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
            {isShareOpen &&
                <ModalAddShare
                    isOpen={isShareOpen}
                    after={() => setIsShareOpen(false)}
                />
            }
        </>
    )

}