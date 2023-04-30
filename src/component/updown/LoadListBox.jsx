import React, { useState } from "react";
import '../../css/load.css'

export default function LoadListBox(props){

    return (
        <div className="LoadListBox">
            <div className="titleT">{props.title}</div>
            <div className="child">
                {props.children}
            </div>
        </div>
    )
    
} 