import React from "react";
import { AiOutlineClose } from 'react-icons/ai'
import '../../css/fileview.css';
export default function ModalFileopen(props) {
    const fileData = props.file;
    return (
        <div className="fileopen">
            <div className="preview">preview needed.</div>
            <div className="fileopen-close" onClick={() => props.after()}><AiOutlineClose/></div>
        </div>
    )

}