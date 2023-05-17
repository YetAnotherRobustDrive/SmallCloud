import React from "react";
import { FcFile, FcFolder } from 'react-icons/fc';
import { useNavigate } from 'react-router-dom';
import '../../css/bodyframe.css';

export default function ListBox(props) {
    const data = props.data;
    const navigate = useNavigate();

    const handleOnClick = () => {
        console.log(data);
        if (data.type == "file") {
            props.onClick();
            return;
        }
        else {
            navigate("files/" + data.id);
        }
    }
    return (
        <div className="listbox" onClick={handleOnClick}>
            <div className="icon">
                {data.type == "file" ? <FcFile /> : <FcFolder />}
            </div>
            <div className="name">{data.name}</div>
            <div className="size">{data.size}</div>
        </div>
    )
}