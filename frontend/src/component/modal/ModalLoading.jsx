import { CircularProgress } from "@material-ui/core";
import React from "react";
import Modal from 'react-modal';
import '../../css/modal.css';


export default function ModalLoading(props) {

    const modalStyle = {
        content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            border: 'none',
            backgroundColor: 'none',
            overflow: 'hidden',
        },
    }


    Modal.setAppElement("#root")

    return (
        <Modal isOpen={props.isOpen} style={modalStyle} >
            <div className="modalOuter">
                <span className="customSpan"><CircularProgress size={"50px"}/></span>
            </div>
        </Modal>
    )
}