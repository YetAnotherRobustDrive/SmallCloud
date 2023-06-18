import React, { useEffect, useState } from "react";
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';
import RefreshToken from "../../services/token/RefreshToken";
import ElevateUser from '../../services/user/ElevateUser';
import GetUserInfo from "../../services/user/GetUserInfo";
import SwalAlert from "../swal/SwalAlert";
import SwalError from "../swal/SwalError";

export default function ModalCheckPW(props) {

    const [isError, setIsError] = useState(false);
    const [isOpen, setIsOpen] = useState(true);
    const [count, setCount] = useState(0);
    const [message, setMessage] = useState(false);

    const [img, setImg] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const getUserInfo = async () => {
            await RefreshToken();

            const res = await GetUserInfo();
            if (!res[0]) {
                SwalAlert("error", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", navigate("/"));
            }
            setImg(res[2]);
        }
        getUserInfo();
    }, [])

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

    const handleSubmit = async (e) => {
        if (e.key === 'Enter') {
            const refreshOk = await RefreshToken();
            if (!refreshOk) {
                SwalError("로그인 정보가 만료되었습니다.");
                navigate('/login');
                return;
            }

            const [isOk, message] = await ElevateUser(e.target.value);
            if (!isOk) { //fail
                setCount(count + 1);
                setMessage(message);
                if (message === 'JWT토큰이 올바르지 않습니다.') {
                    SwalError("로그인 정보가 만료되었습니다.");
                    navigate('/login');
                }
                setIsError(true);
                return;
            }
            setCount(0);
            setIsOpen(false);
            props.after(); //success
        }
    }

    Modal.setAppElement("#root");
    return (
        <>
            <Modal isOpen={isOpen} style={modalStyle}>
                <div className="modalOuter">
                    <div className="imgContainer">
                        <img src={img}></img>
                    </div>
                    <input
                        type="password"
                        placeholder="PW"
                        onKeyDown={handleSubmit}
                        autoFocus
                    />
                    <span>보안을 위해 비밀번호를 입력해주세요.</span>
                    {isError &&
                        <span style={{ color: "red", paddingTop: "5px", textDecoration: "underline" }}>{message + "(" + count + ")"}</span>
                    }
                </div>
            </Modal>
        </>
    )
}