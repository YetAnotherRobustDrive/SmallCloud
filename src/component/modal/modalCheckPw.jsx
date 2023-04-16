import React, { useEffect, useState } from "react";
import Modal from 'react-modal';
import default_profile_img from  '../../img/defalutProfile.png'

export default function ModalCheckPW() {

    const [isModalOpen, setIsModalOpen] = useState(true);
    const [nickname, setNickname] = useState("Nick_name");
    const [img, setImg] = useState(null);

    useEffect(() => {
        if (img === null){
            setImg(default_profile_img);
        }
    })

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

    const clickOK = () => {
        setIsModalOpen(false)
    }
    Modal.setAppElement("#root");
    return (
        <Modal isOpen={isModalOpen} style={modalStyle}>
            <div className="modalOuter">
                <img src={img}></img>
                <span className="nick">{nickname}</span>
                <input 
                type="password" 
                placeholder="PW" 
                onChange={clickOK}
                autoFocus
                />
                <span>보안을 위해 비밀번호를 입력해주세요.</span>
            </div>
        </Modal>
    )
}