import React from "react";
import '../../css/bodyframe.css'
import { FcFolder, FcFile } from 'react-icons/fc'
import { useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';

export default function ListBox(props) {
    const data = props.data;
    const navigate = useNavigate();
    const location = useLocation();

    const handleOnClick = () => {
        if(data.type == "file"){
            props.onClick();
            return;
        }
        else {
            const curr = location.pathname;
            if(curr == "/"){
               navigate(curr + "files/" + data.name);
               return;
            } 
            else {
                if (curr.endsWith('/')) {
                    navigate(curr + data.name);                    
                }
                else {
                    navigate(curr + "/" + data.name);
                }
               return;
            }
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