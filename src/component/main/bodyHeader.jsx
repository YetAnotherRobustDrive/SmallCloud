import React, { useState } from "react";
import '../../css/body.css'
import {TfiViewListAlt, TfiViewGrid} from 'react-icons/tfi'

export default function BodyHeader(props){
    const [sort, setSort] = useState("");//↑↓
    const [view, setView] = useState(""); //TfiViewListAlt TfiViewGrid

    useState(() => {
        if(props.addon == "true"){
            setSort("이름↑");
            setView(<TfiViewGrid/>);
        }
    }, [])

    return (
        <div className="bodyHeader">
            {props.text}
            <div className="options">
                <div>{sort}</div>
                <div>{view}</div>
            </div>
        </div>
    )
    
} 