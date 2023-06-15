import React, { useState } from "react";
import '../../css/admin.css';

export default function RuleInput(props) {

    const [currVal, setCurrVal] = useState(0);
    const [newVal, setNewVal] = useState(0);

    const testTT = (()=>{
        alert("submit!");
    })

    return (
        <div onSubmit={testTT} className="ruleInput">
            <div className="curr">
                <span>현재 {props.desc} : </span>
                <span>{currVal}</span>
            </div>
            <div className="new">
                <span>변경 {props.desc} : </span>
                <input placeholder=""></input>
            </div>
        </div>
    )
}