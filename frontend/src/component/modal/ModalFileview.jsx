import React, { useEffect, useState } from "react";
import { AiFillStar, AiOutlineClose, AiOutlineStar } from "react-icons/ai";
import { BsFillShareFill, BsFillTrashFill } from 'react-icons/bs';
import { GoCloudDownload } from 'react-icons/go';
import { MdGroups, MdOpenInFull, MdPerson } from 'react-icons/md';
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
import ModalAddShare from "./ModalAddShare";
import ModalEmpty from "./ModalEmpty";
import ModalFileopen from "./ModalFileopen";
import ModalGetString from "./ModalGetString";

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
            alert(res[1]);
            return;
        }
        alert("공유가 해제되었습니다.");
        setTimeout(() => window.location.reload(), 250);
    }

    const handleFileRemove = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        if (window.confirm("정말로 파일을 삭제하시겠습니까?")) {
            const res = await PostDeleteFile(fileData.id);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
            alert("파일이 삭제되었습니다.");
            window.location.reload();
        }
    }

    const handleFileRestore = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        if (window.confirm("정말로 파일을 복구하시겠습니까?")) {
            const res = await PostRestoreFile(fileData.id);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
            alert("파일이 복구되었습니다.");
            window.location.reload();
        }
    }

    const handleFavorite = async (e) => {
        e.stopPropagation();
        e.preventDefault();
        if (fileData.isFavorite) {
            const res = await PostUnfavoriteFile(fileData.id);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
        }
        else {
            const res = await PostFavoriteFile(fileData.id);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
        }
        fileData.isFavorite = !fileData.isFavorite;
        if (fileData.isFavorite) {
            alert("즐겨찾기 설정되었습니다.");
        }
        else {
            alert("즐겨찾기 해제되었습니다.");
        }
        window.location.reload();
    }

    useEffect(() => {
        const editLabel = async () => {
            let labelsForPost = [];
            newLables.split(/\s|#/).filter(Boolean).forEach(async (label) => {
                labelsForPost.push(label);
            });
            labelsForPost = [...new Set(labelsForPost)];
            if (labelsForPost.length !== 0) {
                await PostLabelFile(fileData.id, labelsForPost);
                alert("라벨이 수정되었습니다.");
                window.location.reload();
            }
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
                                <div className='icon' onClick={handleFavorite}>{fileData.isFavorite ? <AiFillStar /> : <AiOutlineStar />}</div>
                                <div className='icon' onClick={handleDownload}><GoCloudDownload /></div>
                            </>}
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
                            {props.isDeleted !== true &&
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
                                    {props.isDeleted !== true &&
                                        <button onClick={handleLabelEdit} className="icon" ><TbEdit /></button>
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
                                {props.isDeleted !== true &&
                                    <div className="removeFile" onClick={handleFileRemove}>
                                        <span><BsFillTrashFill /></span>
                                    </div>
                                }
                                {props.isDeleted === true &&
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
                    after={() => { setIsShareOpen(false); setTimeout(() => window.location.reload(), 250);}}
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
