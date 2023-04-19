import React, { useState } from "react";
import '../../css/main.css'

export default function MainHeader(props){

    return (
        <div className="searchbar">
            {props.children}
        </div>
    )
    
} 