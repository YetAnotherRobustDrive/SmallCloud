import React from "react";
import '../../css/bodyframe.css'

export default function NarrowBox(props) {

    return (
        <div className="narrow" onContextMenu={e => {e.stopPropagation(); e.preventDefault();}}>
            {props.children}
        </div>
    )
}