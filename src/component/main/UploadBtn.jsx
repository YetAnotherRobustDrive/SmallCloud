import React, { useState } from "react";
import {AiFillPlusCircle} from 'react-icons/ai'
import '../../css/load.css'

export default function UploadBtn() {

    const datas = [ //replace with fetched data
        { label: "1111" },
        { label: "2222" },
        { label: "3333" },
        { label: "4444" },
        { label: "5555" }
    ];

    const [isOpen, setIsOpen] = useState(false);

    const handleOptionClick = () => {
        setIsOpen(false);
    };

    return (
        <div className="upload-btn">
            <div className="btn-header" onClick={() => setIsOpen(!isOpen)}>
                <button className="btn"><AiFillPlusCircle /></button>
                {isOpen && (
                    <>
                        <div className="title">업로드 처리 목록</div>
                        <ul className="works" onMouseLeave={() => setIsOpen(false)}>
                            {datas.map((option) => (
                                <li
                                    key={option.value}
                                    className="work"
                                    onClick={() => handleOptionClick(option)}
                                >
                                    {option.label}
                                </li>
                            ))}
                        </ul>
                        <button className="upBtn">업로드 추가하기</button>
                    </>
                )}
            </div>
        </div>
    )
}