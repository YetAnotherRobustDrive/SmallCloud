import React, { useState } from "react";
import Modal from 'react-modal';
import '../../css/modal.css'


export default function ModalOk(props) {

    const [isModalOpen, setIsModalOpen] = useState(true);

    const modalClose = () => {
        setIsModalOpen(false)
    }

    const modalStyle = {
        content: {
            top: '10%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
        },
    }


    Modal.setAppElement("#root")

    return (
        <Modal onAfterClose={props.close} isOpen={isModalOpen} style={modalStyle} >
            <div className="modalOuter">
                <span className="customSpan">{props.children}</span>
                <div>
                    <button onClick={modalClose} autoFocus>확인</button>
                </div>
            </div>
        </Modal>
    )
}