import React, { useEffect, useState } from "react";
import '../../css/admin.css';
import {BsToggleOff, BsToggleOn} from 'react-icons/bs'

export default function ToggleBtn(props) {

    const [isOff, setIsOff] = useState(true);
    const [icon, setIcon] = useState(<BsToggleOff/>);

    const testTT = (()=>{
        isOff ? setIsOff(false):setIsOff(true);
        isOff ? setIcon(<BsToggleOn/>):setIcon(<BsToggleOff/>);
        props.onClick();
    })

    return (
        <div onClick={testTT} className="tgBtn">
            <div className="stateText">{isOff ? "OFF" : "ON"}</div>
            {icon}
        </div>
    )
}