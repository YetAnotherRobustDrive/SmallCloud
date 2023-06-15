import React from "react";
import Modal from 'react-modal';
import '../../css/modal.css';


export default function ModalEmpty(props) {

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

    return (
        <Modal onAfterClose={props.close} isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuterDown" onKeyDown={() => {}} autoFocus>
                {props.children}
            </div>
        </Modal>
    )
}