import React, { useEffect, useState } from "react";
import Modal from 'react-modal';
import default_profile_img from '../../img/defalutProfile.png';
import ElevateUser from '../../services/user/ElevateUser';

export default function ModalCheckPW(props) {

    const [isError, setIsError] = useState(false);
    const [count, setCount] = useState(0);
    const [message, setMessage] = useState(false);
    const [nickname, setNickname] = useState("Nick_name");
    const [img, setImg] = useState(null);

    useEffect(() => {
        if (img === null) {
            setImg(default_profile_img);
        }
        console.log(props.isOpen);
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
        if (e.key == 'Enter') {
            const [isOk, messsage] = await ElevateUser(e.target.value);
            if (!isOk) { //fail
                setCount(count + 1);
                setMessage(messsage);
                setIsError(true);
                
                return;
            }
            setCount(0);
            props.after(); //success
        }
    }

    Modal.setAppElement("#root");
    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuter">
                <img src={img}></img>
                <span className="nick">{nickname}</span>
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
    )
}