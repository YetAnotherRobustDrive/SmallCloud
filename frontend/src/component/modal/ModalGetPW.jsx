import React from "react";
import { AiOutlineClose } from 'react-icons/ai';
import Modal from 'react-modal';
import '../../css/modal.css';
import PostChangePw from "../../services/user/PostChangePw";

export default function ModalGetPW(props) {

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
    Modal.setAppElement("#root")

    const handleClose = (e) => {
        e.preventDefault();
        props.after();
    }
    
    const handleSubmit = async (e) => {
        e.preventDefault();
        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        if (value.newPassword !== value.newPasswordConfirm) {
            alert("새 비밀번호가 일치하지 않습니다.");
            return;
        }
        const res = await PostChangePw(value);
        if (res[0]) {
            alert("비밀번호가 변경되었습니다.");
            props.after();
        }
        else {
            alert(res[1]);
        }
    }

    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <form className="modalOuter" onSubmit={handleSubmit}>
                <div className="modalShareHeader">
                    <span className="title">{props.title}</span>
                    <div className="close" onClick={handleClose}><AiOutlineClose /></div>
                </div>
                <input
                    style={{marginBottom: "0"}}
                    name="password"
                    type="password"
                    placeholder={"현재 비밀번호"}
                    autoFocus
                />
                <input
                    style={{marginBottom: "10px"}}
                    name="newPassword"
                    type="password"
                    placeholder={"새 비밀번호"}
                />
                <input
                    style={{marginTop: "0"}}
                    name="newPasswordConfirm"
                    type="password"
                    placeholder={"새 비밀번호 확인"}
                />
                <button>확인</button>
            </form>
        </Modal>
    )
}