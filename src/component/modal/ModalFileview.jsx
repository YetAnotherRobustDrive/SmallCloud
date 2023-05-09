import React, { useState } from "react";
import '../../css/fileview.css';
import ModalFileopen from "./ModalFileopen";
import { AiOutlineClose } from "react-icons/ai";

export default function ModalFileview(props) {
    const [isFileOpen, setIsFileOpen] = useState(false);
    const [isGeneralSelected, setIsGeneralSelected] = useState(true);
    const fileData = props.file;

    return (
        <div className="fileview">
            <div className='head'>
                <span className='filename'>nameeeeeeeeeeeeeeeeeeeeeee</span>
                <div className="fileBtn">
                    <div className='open' onClick={() => setIsFileOpen(true)}>열기</div>
                    <div className='close' onClick={() => props.after()}><AiOutlineClose /></div>
                </div>
            </div>
            <div className="body">
                <div className="options">
                    <span
                        style={isGeneralSelected ? { textDecoration: "underline" } : {}}
                        onClick={() => setIsGeneralSelected(true)}>
                        일반
                    </span>
                    <span
                        style={!isGeneralSelected ? { textDecoration: "underline" } : {}}
                        onClick={() => setIsGeneralSelected(false)}>
                        공유
                    </span>
                </div>

            </div>
            {isFileOpen &&
                <ModalFileopen
                    after={() => setIsFileOpen(false)}
                />
            }
        </div>
    )

}