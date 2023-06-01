import React from "react";
import { AiOutlineClose } from 'react-icons/ai';
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
                <div className="modalShareHeader">
                    <span className="title">{props.title}</span>
                    <div className="close" onClick={handleClose}><AiOutlineClose /></div>
                </div>
                <input
                    defaultValue={props.defaultValue}
                    type="text"
                    placeholder={props.placeholder}
                    onKeyDown={handleKeyDown}
                    autoFocus
                />
            </div>
        </Modal>
    )
}