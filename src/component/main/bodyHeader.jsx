import React, { useState } from "react";
import '../../css/body.css'

export default function BodyHeader(props){

    return (
        <div className="bodyHeader">
            {props.text}
        </div>
    )
    
} 