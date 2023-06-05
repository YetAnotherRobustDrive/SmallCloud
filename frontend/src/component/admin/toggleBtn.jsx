import React, { useEffect, useState } from "react";
import '../../css/admin.css';
import {BsToggleOff, BsToggleOn} from 'react-icons/bs'

export default function ToggleBtn(props) {

    const [isLocked, setIsLocked] = useState(props.default === undefined ? true : props.default);
    const [icon, setIcon] = useState(props.default ? <BsToggleOn/> : <BsToggleOff/>);
    console.log("props", props.default);

    const testTT = (()=>{
        setIsLocked(!isLocked);
        if (isLocked) {
            setIcon(<BsToggleOn/>);
        } else {
            setIcon(<BsToggleOff/>);
        }
        props.onClick();
    })

    return (
        <div onClick={testTT} className="tgBtn">
            <div className="stateText">{!isLocked ? "OFF" : "ON"}</div>
            {icon}
        </div>
    )
}