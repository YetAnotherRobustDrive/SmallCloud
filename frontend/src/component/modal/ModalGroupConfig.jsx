import React, { useState } from "react";
import { AiOutlineClose } from 'react-icons/ai';
import { MdGroups } from 'react-icons/md';
import Modal from 'react-modal';
import '../../css/modal.css';
import GetSearchGroup from "../../services/group/GetSearchGroup";
import AdminGroupRm from "../../services/admin/AdminGroupRm";
import jwtDecode from "jwt-decode";

export default function ModalGroupConfig(props) {

    const [searched, setSearched] = useState([]);

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
            const groupSearch = await GetSearchGroup(e.target.value);
            if (!groupSearch[0]) {
                return;
            }
            const group = groupSearch[1].map((d) => {
                return {
                    "name": d,
                }
            })
            setSearched([...group]);
        }
        else {
            setSearched([]);
        }
    }

    const handleKeyDown = (e) => {
        if (e.key === "Escape") {
            e.preventDefault();
            setSearched([]);
            e.target.value = "";
        }
    }

    const handleRemove = async () => {
        if (props.curr === null || props.curr === "없음") {
            alert("소속된 그룹이 없습니다.");
            return;
        } 

        const res = await AdminGroupRm(props.curr, props.userName);
        if (!res[0]) {
            alert(res[1]);
            return;
        }
        alert("그룹이 제거되었습니다.");
        props.setter("없음");
        props.after();
    }

    const handleClick = (e) => {
        const name = e.currentTarget.querySelector(".name").innerText;
        props.setter(name);
        props.after();
    }

    Modal.setAppElement("#root");
    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuter" >
                <div className="modalShareHeader">
                    <span className="title">그룹 설정하기</span>
                    <div className="close" onClick={handleClose}><AiOutlineClose /></div>
                </div>
                <div className="modalShareBody modalGroup">
                    <input
                        id="shareInput"
                        className="shareInput"
                        type="text"
                        placeholder="그룹의 이름을 입력하세요."
                        onKeyDown={handleKeyDown}
                        onChange={handleOnChange} />
                    {searched.length !== 0 &&
                        <div className="searchResult">
                            {searched.map((item, index) => {
                                return (
                                    <div className="resultItem" key={index} onClick={handleClick}>
                                        <div className="icon"><MdGroups /></div>
                                        <span className="name">{item.name}</span>
                                    </div>
                                )
                            })}
                        </div>
                    }
                </div>
                <button onClick={handleRemove}>그룹 제거하기</button>
            </div>
        </Modal>
    )
}