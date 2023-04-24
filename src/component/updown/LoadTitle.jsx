import React, { useState } from "react";
import '../../css/load.css'

export default function LoadTitleBox(props){

    return (
        <div className="loadTitleBox">
            <div className="titleT">{props.title}</div>
            <div className="child">
                {props.children}
            </div>
        </div>
    )
    
} 