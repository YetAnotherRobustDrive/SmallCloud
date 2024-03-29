import React, { useState } from "react";
import { BsToggleOff, BsToggleOn } from 'react-icons/bs';
import '../../css/admin.css';

export default function ToggleBtn(props) {

    const [isLocked, setIsLocked] = useState(props.default === undefined ? false : props.default);

    const handleOnClick = (()=>{
        setIsLocked(!isLocked);
        props.onClick();
    })

    return (
        <div onClick={handleOnClick} className="tgBtn">
            <div className="stateText">{!isLocked ? "OFF" : "ON"}</div>
            {!isLocked ? <BsToggleOff /> : <BsToggleOn />}
        </div>
    )
}