import React from "react";
import { AiOutlineClose } from 'react-icons/ai';
import Modal from 'react-modal';
import '../../css/modal.css';
import PostChangePw from "../../services/user/PostChangePw";
import SwalError from "../swal/SwalError";
import SwalAlert from "../swal/SwalAlert";
import GetConfig from "../../services/config/GetConfig";

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
        if (value.newPassword === "" || value.newPasswordConfirm === "" || value.password === "") {
            SwalError("새 비밀번호를 입력해주세요.");
            return;
        }
        
        if (value.newPassword !== value.newPasswordConfirm) {
            SwalError("새 비밀번호가 일치하지 않습니다.");
            return;
        }

        const configRes = await GetConfig("201");
        if (!configRes[0]) {
            SwalError(configRes[1]);
            return;
        }
        const isCombinationNeeded = (configRes[1] === "true");
        if (isCombinationNeeded) {
            if (!value.newPassword.match(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9])/)) {
                SwalError("비밀번호는 영문+숫자+특수문자 조합으로 입력해주세요.");
                return;
            }
        }

        const configRes2 = await GetConfig("202");
        if (!configRes2[0]) {
            SwalError(configRes2[1]);
            return;
        }
        const minimumLength = parseInt(configRes2[1]);
        if (minimumLength > value.newPassword.length) {
            SwalError("비밀번호는 " + minimumLength + "자 이상으로 입력해주세요.");
            return;
        }

        const res = await PostChangePw(value);
        if (res[0]) {
            SwalAlert("success", "비밀번호가 변경되었습니다.", props.after)
        }
        else {
            SwalError(res[1]);
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