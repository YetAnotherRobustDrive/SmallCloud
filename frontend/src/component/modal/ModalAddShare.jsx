import React from "react";
import Modal from 'react-modal';
import { AiOutlineClose } from 'react-icons/ai'
import '../../css/modal.css'

export default function ModalAddShare(props) {

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

    Modal.setAppElement("#root");
    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuter">
                <div className="modalShareHeader">
                    <span className="title">공유 추가하기</span>
                    <div className="close" onClick={handleClose}><AiOutlineClose /></div>
                </div>
                <div className="modalShareBody">
                    <input className="shareInput" type="text" placeholder="공유할 사용자나 그룹의 이름을 입력하세요." />
                    <div className="searchResult">
                        asdf
                    </div>
                </div>
            </div>
        </Modal>
    )
}