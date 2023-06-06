import React, { useEffect, useState } from "react";
import { AiOutlineClose } from 'react-icons/ai';
import { MdGroups, MdPerson } from 'react-icons/md';
import Modal from 'react-modal';
import '../../css/modal.css';
import GetDirInfo from "../../services/directory/GetDirInfo";
import GetSearchGroup from "../../services/group/GetSearchGroup";
import PostCreateShare from "../../services/share/PostCreateShare";
import PostDeleteShare from "../../services/share/PostDeleteShare";
import GetSearchUser from "../../services/user/GetSearchUser";

export default function ModalFolderShare(props) {

    const [searched, setSearched] = useState([]);
    const [candidate, setCandidate] = useState([]);
    const [original, setOriginal] = useState([]);

    const modalStyle = {
        content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
        },
    }

    useEffect(() => {
        const getShare = async () => {
            const res = await GetDirInfo(props.folderID);
            if (!res[0]) {
                alert(res[1]);
                return;
            }
            const shares = res[1].shares;
            const tmp = shares.map((d) => {
                return {
                    "targetName": d.targetName,
                    "type": d.type === "MemberShare" ? "MEMBER" : "GROUP",
                }
            }
            )
            setCandidate(tmp);
            setOriginal(tmp);
        }
        getShare();
    }, [])

    const handleClose = () => {
        props.after();
    }

    const handleOnChange = async (e) =>  {
        if (e.target.value !== "") {
            const userSearch = await GetSearchUser(e.target.value);
            const groupSearch = await GetSearchGroup(e.target.value);
            if (!userSearch[0] || !groupSearch[0]) {
                return;
            }
            const user = userSearch[1].map((d) => {
                return {
                    "name": d,
                    "type": "MEMBER",
                }
            })
            const group = groupSearch[1].map((d) => {
                return {
                    "name": d,
                    "type": "GROUP",
                }
            })
            setSearched([...user, ...group]);
        }
        else {
            setSearched([]);
        }
    }

    const addCandidate = (name, type) => {
        window.document.getElementById("shareInput").value = "";
        setSearched([]);
        const res = candidate.find((e) => e.targetName === name && e.type === type);
        if (res === undefined) {
            setCandidate([
                ...candidate,
                {
                    "targetName": name,
                    "type": type,
                }
            ]);            
        }
    }

    const handleKeyDown = (e) => {
        if (e.key === "Escape") {
            e.preventDefault();
            setSearched([]);
            e.target.value = "";
        }
    }

    const handleClick = (e) => {
        e.stopPropagation();
        e.preventDefault();
        const idx = e.currentTarget.id;
        let tmp = [...candidate];
        tmp.splice(idx, 1);
        setCandidate(tmp);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const delList = original.filter((d) => {
            const res = candidate.find((e) => e.targetName === d.targetName);
            if (res === undefined) {
                return true;
            }
            return false;
        })
        const newList = candidate.filter((d) => {
            const res = original.find((e) => e.targetName === d.targetName);
            if (res === undefined) {
                return true;
            }
            return false;
        })
        delList.forEach(async (d) => {
            const data = {
                "fileId": props.folderID,
                "targetName": d.targetName,
                "type": d.type
            }
            await PostDeleteShare(data);
        })
        newList.forEach(async (d) => {
            const data = {
                "fileId": props.folderID,
                "targetName": d.targetName,
                "type": d.type
            }
            await PostCreateShare(data);
        })
        alert("공유가 적용되었습니다.");
        props.after();
    }

    Modal.setAppElement("#root");
    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuter" >
                <div className="modalShareHeader">
                    <span className="title">공유 관리하기</span>
                    <div className="close" onClick={handleClose}><AiOutlineClose /></div>
                </div>
                <div className="modalShareBody">
                    <input
                        id="shareInput"
                        className="shareInput"
                        type="text"
                        placeholder="추가할 사용자나 그룹의 이름을 입력하세요."
                        onKeyDown={handleKeyDown}
                        onChange={handleOnChange} />
                    {searched.length !== 0 &&
                        <div className="searchResult">
                            {searched.map((item, index) => {
                                return (
                                    <div className="resultItem" key={index} onClick={() => addCandidate(item.name, item.type)}>
                                        <div className="icon">{item.type === "MEMBER" ? <MdPerson /> : <MdGroups />}</div>
                                        <span className="name">{item.name}</span>
                                    </div>
                                )
                            })}
                        </div>
                    }
                    <div className="candidates">
                        {candidate.length === 0 ?
                            "공유 대상이 없습니다." :
                            candidate.map((item, index) => {
                                return (
                                    <div className="resultItem" key={index}>
                                        <div className="icon">{item.type === "MEMBER" ? <MdPerson /> : <MdGroups />}</div>
                                        <span className="name">{item.targetName}</span>
                                        <div id={index} className="close" onClick={handleClick}><AiOutlineClose /></div>
                                    </div>
                                )
                            })}
                    </div>
                </div>
                <button onClick={handleSubmit}>적용하기</button>
            </div>
        </Modal>
    )
}