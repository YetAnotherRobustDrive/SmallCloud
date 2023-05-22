import React from "react";
import { TfiViewGrid, TfiViewListAlt } from 'react-icons/tfi';
import '../../css/body.css';
import SortDropdown from "../dropdown/SortDropdown";

export default function BodyHeader(props) {

    return (
        <div className="bodyHeader">
            <div className="text">
                {props.text}
            </div>
            {props.addon && (
                <div className="options">
                    <div className="option"><SortDropdown /></div>
                    <div className="option" onClick={() => props.addon(!props.view)}>{props.view ? <TfiViewListAlt /> : <TfiViewGrid />}</div>
                </div>)
            }
        </div>
    )

} 