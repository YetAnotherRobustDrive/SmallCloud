import React from "react";
import Modal from 'react-modal';
import '../../css/modal.css';

export default function ModalGetString(props) {

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

    const handleKeyDown = (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            props.setter(e.target.value);
            props.after();
        }
    }

    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuter">
                <span>{props.title}</span>
                <input
                    defaultValue={props.defaultValue}
                    type="text"
                    placeholder={props.placeholder}
                    onKeyDown={handleKeyDown}
                    autoFocus
                />
                <div className="getStrClose" onClick={handleClose}>닫기</div>
            </div>
        </Modal>
    )
}