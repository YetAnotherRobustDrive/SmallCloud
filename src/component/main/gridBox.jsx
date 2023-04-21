import React from "react";
import '../../css/bodyframe.css'

export default function GridBox(props) {

    return (
        <div className="gridscroll">
            <div className="gridbox">
                {props.children}
            </div>
        </div>
    )
}