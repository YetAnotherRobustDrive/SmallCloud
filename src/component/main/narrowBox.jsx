import React from "react";

export default function NarrowBox(props) {
    return (
        <div style={{ height: "100px", display:"flex" }}>
            {props.children}
        </div>
    )
}
