import React, { useState } from "react";
import Modal from 'react-modal';

export default function ModalConfirmRemove() {

    const [isModalOpen, setIsModalOpen] = useState(true);

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

    const clickCancel = () => {
        setIsModalOpen(false)
    }
    Modal.setAppElement("#root")

    return (
        <Modal isOpen={isModalOpen} style={modalStyle}> 
            <div className="modalOuter">
                <span>정말로 삭제하시겠습니까?</span>
                <div>
                <button onClick={clickCancel}>확인</button>
                <button onClick={clickOK}>취소</button>
                </div>
            </div>
        </Modal>
    )
}