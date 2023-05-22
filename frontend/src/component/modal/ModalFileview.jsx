import React, { useState } from "react";
import '../../css/fileview.css';
import ModalFileopen from "./ModalFileopen";
import { AiOutlineClose } from "react-icons/ai";
import { MdOpenInFull } from "react-icons/md";

export default function ModalFileview(props) {
    const [isFileOpen, setIsFileOpen] = useState(false);
    const [isGeneralSelected, setIsGeneralSelected] = useState(true);
    const fileData = props.file;

    return (
        <div className="fileview">
            <div className='head'>
                <span className='filename'>{fileData.name}</span>
                <div className="fileBtn">
                    <div className='icon' onClick={() => setIsFileOpen(true)}><MdOpenInFull /></div>
                    <div className='icon' onClick={() => props.after()}><AiOutlineClose /></div>
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
                {isGeneralSelected &&
                    <>
                        <div>{fileData.id}</div>
                        <div>{fileData.securityLevel}</div>
                        <div>{fileData.writingStage}</div>
                        <div>{fileData.size}</div>
                    </>
                }
                {!isGeneralSelected &&
                    <>
                        <div>{fileData.shared ? "공유 O" : "공유 X"}</div>
                    </>
                }
            </div>
            {isFileOpen &&
                <ModalFileopen
                    after={() => setIsFileOpen(false)}
                />
            }
        </div>
    )

}