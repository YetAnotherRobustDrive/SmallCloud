import React, { useState } from "react";
import '../../css/body.css'
import { TfiViewListAlt, TfiViewGrid } from 'react-icons/tfi'
import SortDropdown from "../dropdown/SortDropdown";

export default function BodyHeader(props) {

    const [view, setView] = useState(false); //TfiViewListAlt TfiViewGrid

    function clickView() {
        setView(!view)
        //fetch here
    }

    return (
        <div className="bodyHeader">
            {props.text}
            {props.addon && (
                <div className="options">
                    <div className="option"><SortDropdown /></div>
                    <div className="option" onClick={clickView}>{view ? <TfiViewListAlt /> : <TfiViewGrid />}</div>
                </div>)
            }
        </div>
    )

} 