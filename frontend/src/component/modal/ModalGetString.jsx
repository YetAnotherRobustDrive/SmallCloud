import React, { useState } from "react";
import Modal from 'react-modal';

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

    return (
        <Modal isOpen={props.isOpen} style={modalStyle}>
            <div className="modalOuter">
                <span>이름 입력</span>
                <input
                    type="text"
                    placeholder="text"
                    onKeyDown={(e) => {
                        if (e.key == "Enter") {
                            props.setter(e.target.value);
                            props.after();                   
                        }
                    }}
                    autoFocus
                />
            </div>
        </Modal>
    )
}