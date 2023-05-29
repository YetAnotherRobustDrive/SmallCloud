import React, { useState } from "react";
import { AiFillPlusCircle } from 'react-icons/ai'
import '../../css/load.css'

export default function UploadBtn() {

    const [isOpen, setIsOpen] = useState(false);

    const handleDrop = (e) => {
        e.preventDefault();
        console.log(e.dateTransfer);
    }

    return (
        <div className="upload-btn">
            <div className="btn-header" >
                {isOpen && (
                    <div className="open-space">
                        <div className="bay" onDrop={handleDrop}/>
                        <button className="upBtn">업로드</button>
                    </div>
                )}
                <div className="btn" onClick={() => setIsOpen(!isOpen)}><AiFillPlusCircle /></div>
            </div>
        </div>
    )
}