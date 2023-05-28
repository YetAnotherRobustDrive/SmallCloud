import React from "react";

export default function ContextMenu(props) {

    return (
        <div style={{border:"1px solid black", backgroundColor:"white"}}>
            <ul >
                {props.options.map((option, idx) => (
                    <li key={idx}>
                        {option}
                    </li>
                ))}
            </ul>
        </div>
    )
}