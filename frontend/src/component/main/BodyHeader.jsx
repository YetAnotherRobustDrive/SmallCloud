import React from "react";
import '../../css/body.css';
import SortDropdown from "../dropdown/SortDropdown";

export default function BodyHeader(props) {

    return (
        <div className="bodyHeader" onContextMenu={e => { e.stopPropagation(); e.preventDefault(); }}>
            <div className="text">
                {props.text}
            </div>
            {props.isSortable !== undefined && (
                <div className="options">
                    <div className="option" ><SortDropdown setter={props.setter}/></div>
                </div>)
            }
        </div>
    )

} 