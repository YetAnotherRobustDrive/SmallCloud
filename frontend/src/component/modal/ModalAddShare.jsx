import React, { useState } from "react";
import Modal from 'react-modal';
import { AiOutlineClose } from 'react-icons/ai';
import { MdPerson, MdGroups } from 'react-icons/md';
import '../../css/modal.css';
import GetSearchUser from "../../services/user/GetSearchUser";
import GetSearchGroup from "../../services/group/GetSearchGroup";
import PostCreateShare from "../../services/share/PostCreateShare";
import SwalError from "../swal/SwalError";
import SwalAlert from "../swal/SwalAlert";

export default function ModalAddShare(props) {

    const [searched, setSearched] = useState([]);
    const [candidate, setCandidate] = useState([]);

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
        candidate.forEach(async (d) => {
            const data = {
                "fileId": props.fileID,
                "targetName": d.targetName,
                "type": d.type
            }
            const res = await PostCreateShare(data);
            if (!res[0]) {
                SwalError("공유 추가에 실패하였습니다.");
                return;
            }
        })
        SwalAlert("success", "공유 추가에 성공하였습니다.", props.after);
    }

    Modal.setAppElement("#root");
    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuter" >
                <div className="modalShareHeader">
                    <span className="title">공유 추가하기</span>
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
                <button onClick={handleSubmit}>추가하기</button>
            </div>
        </Modal>
    )
}