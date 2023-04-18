import React, { useEffect, useState } from "react";
import Modal from 'react-modal';
import {MdOutlinePersonOutline, MdOutlineGroup} from 'react-icons/md'

export default function ModalShare() {

    const [isModalOpen, setIsModalOpen] = useState(true);
    const [nickname, setNickname] = useState("Nick_name");
    const [img, setImg] = useState(null);

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
            <MdOutlinePersonOutline/>
            <MdOutlineGroup/>
        </Modal>
    )
}