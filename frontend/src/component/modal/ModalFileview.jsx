import React, { useEffect, useState } from "react";
import { AiFillStar, AiOutlineClose, AiOutlineStar } from "react-icons/ai";
import { BsFillShareFill, BsFillTrashFill } from 'react-icons/bs';
import { GoCloudDownload } from 'react-icons/go';
import { MdGroups, MdOpenInFull, MdPerson } from 'react-icons/md';
import { TbFileShredder } from 'react-icons/tb';
import { RiArrowGoBackLine } from 'react-icons/ri';
import { TbEdit } from 'react-icons/tb';
import ProgressBar from "../../component/updown/ProgressBar";
import '../../css/fileview.css';
import GetDownloadFile from "../../services/file/GetDownloadFile";
import PostDeleteFile from "../../services/file/PostDeleteFile";
import PostFavoriteFile from "../../services/file/PostFavoriteFile";
import PostLabelFile from "../../services/file/PostLabelFile";
import PostRestoreFile from "../../services/file/PostRestoreFile";
import PostUnfavoriteFile from "../../services/file/PostUnfavoriteFile";
import PostDeleteShare from "../../services/share/PostDeleteShare";
import SwalAlert from "../swal/SwalAlert";
import SwalError from "../swal/SwalError";
import ModalAddShare from "./ModalAddShare";
import ModalEmpty from "./ModalEmpty";
import ModalFileopen from "./ModalFileopen";
import ModalGetString from "./ModalGetString";
import SwalConfirm from "../swal/SwalConfirm";
import PostPurgeFile from "../../services/file/PostPurgeFile";

export default function ModalFileview(props) {
    const [isFileOpen, setIsFileOpen] = useState(false);
    const [isShareOpen, setIsShareOpen] = useState(false);
    const [isLabelEditOpen, setIsLabelEditOpen] = useState(false);
    const [newLables, setNewLabels] = useState("");
    const [isGeneralSelected, setIsGeneralSelected] = useState(true);
    const [isNowDownload, setIsNowDownload] = useState(false);
    const [percentage, setPercentage] = useState(0);
    const [shares, setShares] = useState([]);
    const fileData = props.file;

    const handleDownload = async (e) => {
        setIsNowDownload(true);
        await GetDownloadFile(fileData.id, setPercentage, () => { }, fileData.name)
        setTimeout(() => setIsNowDownload(false), 250);
    }

    const handleLabelEdit = (e) => {
        e.stopPropagation();
        e.preventDefault();
        setIsLabelEditOpen(true);
    }

    const handleShare = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        setIsShareOpen(true);
    }

    const handleShareDelete = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        const fileId = fileData.id;
        const targetName = fileData.shares[e.currentTarget.id].targetName;
        const type = fileData.shares[e.currentTarget.id].type === "MemberShare" ? "MEMBER" : "GROUP";
        const value = {
            fileId: fileId,
            targetName: targetName,
            type: type,
        }
        const res = await PostDeleteShare(value);
        if (!res[0]) {
            SwalError(res[1]);
            return;
        }
        SwalAlert("success", "공유가 해제되었습니다.", () => window.location.reload());
    }

    const handleFileRemove = async (e) => {
        e.stopPropagation();
        e.preventDefault();

        SwalConfirm("정말로 파일을 삭제하시겠습니까?", async () => {
            const res = await PostDeleteFile(fileData.id);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            SwalAlert("success", "파일이 삭제되었습니다.", () => window.location.reload());
        }, () => { });
    }

    const handleFileRestore = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        SwalConfirm("정말로 파일을 복구하시겠습니까?", async () => {
            const res = await PostRestoreFile(fileData.id);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            SwalAlert("success", "파일이 복구되었습니다.", () => window.location.reload());
        }, () => { });
    }

    const handleFavorite = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        if (fileData.isFavorite) {
            const res = await PostUnfavoriteFile(fileData.id);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
        }
        else {
            const res = await PostFavoriteFile(fileData.id);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
        }
        fileData.isFavorite = !fileData.isFavorite;
        if (fileData.isFavorite) {
            SwalAlert("success", "즐겨찾기 되었습니다.", () => window.location.reload());
        }
        else {
            SwalAlert("success", "즐겨찾기 해제되었습니다.", () => window.location.reload());
        }
    }

    const handlePurge = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        SwalConfirm("정말로 파일을 영구 삭제하시겠습니까?", async () => {
            const res = await PostPurgeFile(fileData.id);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            SwalAlert("success", "파일이 영구 삭제되었습니다.", () => window.location.reload());
        }, () => { });
    }

    const handleLabelPrune = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        SwalConfirm("정말로 파일의 라벨을 모두 삭제하시겠습니까?", async () => {
            const res = await PostLabelFile(fileData.id, []);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            SwalAlert("success", "라벨이 모두 삭제되었습니다.", () => window.location.reload());
        }, () => { });
    }

    useEffect(() => {
        const editLabel = async () => {
            if (newLables === "") {
                return;
            }
            let labelsForPost = [];
            newLables.split(/\s|#/).filter(Boolean).forEach(async (label) => {
                labelsForPost.push(label);
            });
            labelsForPost = [...new Set(labelsForPost)];
            await PostLabelFile(fileData.id, labelsForPost);
            SwalAlert("success", "라벨이 수정되었습니다.", () => window.location.reload());
        }
        editLabel();
    }, [newLables])

    useEffect(() => {
        setShares(fileData.shares);
    }, [shares])

    return (
        <>
            {!isNowDownload &&
                <div className="fileview" >
                    <div className='head'>
                        <span className='filename'>{fileData.name}</span>
                    </div>
                    <div className='head'>
                        <div className="fileBtn">
                            {props.isDeleted !== true && <>
                                {fileData.isShared !== true &&
                                    <div className='icon' onClick={handleFavorite}>{fileData.isFavorite ? <AiFillStar /> : <AiOutlineStar />}</div>
                                }
                                <div className='icon' onClick={handleDownload}><GoCloudDownload /></div>
                            </>}
                            {props.isDeleted === true && <div className='icon' onClick={handlePurge}><TbFileShredder /></div>}
                            <div className='icon' onClick={() => setIsFileOpen(true)}><MdOpenInFull /></div>
                            <div className='icon' onClick={() => {

                                window.document.querySelector("video").hidden = true;
                                window.document.querySelector("video").pause();
                                props.after()
                            }}><AiOutlineClose /></div>
                        </div>
                    </div>
                    <div className="body">
                        <div className="options">
                            <span
                                style={isGeneralSelected ? { textDecoration: "underline" } : {}}
                                onClick={() => setIsGeneralSelected(true)}>
                                일반
                            </span>
                            {(props.isDeleted !== true && fileData.isShared !== true) &&
                                <span
                                    style={!isGeneralSelected ? { textDecoration: "underline" } : {}}
                                    onClick={() => setIsGeneralSelected(false)}>
                                    공유
                                </span>
                            }
                        </div>
                        {isGeneralSelected && //일반
                            <>
                                <div className="labels">
                                    <span>라벨</span>
                                    {(props.isDeleted !== true && fileData.isShared !== true) &&
                                        <>
                                            <button onClick={handleLabelEdit} className="icon" ><TbEdit /></button>
                                            <button onClick={handleLabelPrune} className="icon" style={{marginLeft: "10px"}}><AiOutlineClose /></button>
                                        </>
                                    }
                                    <div className="label">
                                        {fileData.labels.length === 0 ? "없음" : fileData.labels.map((label, index) => {
                                            return ("#" + label.name + " ");
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
                                {(props.isDeleted !== true && fileData.isShared !== true) &&
                                    <div className="removeFile" onClick={handleFileRemove}>
                                        <span><BsFillTrashFill /></span>
                                    </div>
                                }
                                {(props.isDeleted === true && fileData.isShared !== true) &&
                                    <div className="removeFile" onClick={handleFileRestore}>
                                        <span><RiArrowGoBackLine /></span>
                                    </div>
                                }
                            </>
                        }
                        {(!isGeneralSelected && props.isDeleted !== true) && //공유
                            <>
                                <div className='share-icon' onClick={handleShare}><BsFillShareFill /></div>
                                <div className="shareBody">
                                    <span>현재 공유</span>
                                    <div className="shareList">
                                        {fileData.shares.length === 0 ?
                                            "현재 공유대상이 없습니다." :
                                            fileData.shares.map((item, index) => {
                                                return (
                                                    <div className="shareItem" key={index}>
                                                        <div className="icon">{item.type === "MemberShare" ? <MdPerson /> : <MdGroups />}</div>
                                                        <span className="name">{item.targetName}</span>
                                                        <div className="deleteIcon" id={index} onClick={handleShareDelete}><AiOutlineClose /></div>
                                                    </div>
                                                )
                                            })
                                        }
                                    </div>
                                </div>
                            </>
                        }
                    </div>
                    {isFileOpen &&
                        <ModalFileopen
                            id={fileData.id}
                            type={fileData.name.substring(fileData.name.lastIndexOf(".") + 1, fileData.name.length)}
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
                    fileID={fileData.id}
                    isOpen={isShareOpen}
                    after={() => { setIsShareOpen(false); setTimeout(() => window.location.reload(), 250); }}
                />
            }
            {isLabelEditOpen &&
                <ModalGetString
                    defaultValue={fileData.labels.map((label, index) => {
                        return " " + label.name;
                    })}
                    title={"라벨 수정하기"}
                    placeholder={"#라벨1 #라벨2 #라벨3"}
                    setter={setNewLabels}
                    isOpen={isLabelEditOpen}
                    after={() => setIsLabelEditOpen(false)}
                />
            }
        </>
    )

}
