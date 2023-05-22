import React, { useState } from "react";
import {AiFillPlusCircle} from 'react-icons/ai'
import '../../css/load.css'

export default function UploadBtn() {

    const datas = [ //replace with fetched data
        { id: "1", label: "1111" },
        { id: "2", label: "2222" },
        { id: "3", label: "3333" },
        { id: "4", label: "4444" },
        { id: "5", label: "5555" }
    ];

    const [isOpen, setIsOpen] = useState(false);

    return (
        <div className="upload-btn">
            <div className="btn-header" >
                {isOpen && (
                    <div className="open-space">
                        <div className="title">업로드 처리 목록</div>
                        <ul className="works" >
                            {datas.map((data) => (
                                <li
                                    key={data.id}
                                    className="work"
                                >
                                    {data.label}
                                </li>
                            ))}
                        </ul>
                        <div className="upBtn">업로드 추가</div>
                    </div>
                )}
                <div className="btn" onClick={() => setIsOpen(!isOpen)}><AiFillPlusCircle /></div>
            </div>
        </div>
    )
}