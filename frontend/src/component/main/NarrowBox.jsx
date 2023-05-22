import React from "react";
import '../../css/bodyframe.css'

export default function NarrowBox(props) {

    return (
        <div className="narrow">
            {props.children}
        </div>
    )
}