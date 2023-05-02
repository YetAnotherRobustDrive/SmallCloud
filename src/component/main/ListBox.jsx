import React from "react";
import '../../css/bodyframe.css'
import { FcFolder, FcFile } from 'react-icons/fc'

export default function ListBox(props) {
    const data = props.data;
    return (
        <div className="listbox">
            <div className="icon">
                {data.type == "file" ? <FcFile /> : <FcFolder />}
            </div>
            <div className="name">{data.name}</div>
            <div className="size">{data.size}</div>
        </div>
    )
}