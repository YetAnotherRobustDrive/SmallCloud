import React, { useEffect, useState } from "react";
import { FcFile, FcFolder } from 'react-icons/fc';
import { useNavigate } from "react-router-dom";
import '../../css/customIcon.css';
import ContextFile from "../contextMenu/ContextFile";
import ContextFolder from "../contextMenu/ContextFolder";

export default function CustomIcon(props) {

    const [icon, setIcon] = useState(null);
    const [isContextOpen, setIsContextOpen] = useState(false);

    const navigate = useNavigate();

    const handleOnClick = (e) => {
        e.preventDefault();
        if (props.type === "file") {
            props.onClick();
            return;
        }
        else {
            navigate("/files/" + props.id);
            return;
        }
    }

    useEffect(() => {
        setIcon(
            props.type === "file" ? <FcFile /> : <FcFolder />
        )
    }, [])

    const handelDrop = (e) => {
        e.preventDefault();
        if (props.type === "folder") {
            props.targetSetter(props.id);
        }
    }

    const handelDragEnd = (e) => {
        e.preventDefault();
        props.sourceSetter(props.id);
    }
    const handelDragOver = (e) => {
        e.preventDefault();
    }

    const handleOnContextMenu = (e) => {
        e.preventDefault();
        e.stopPropagation(); 
        isContextOpen ? setIsContextOpen(false) : setIsContextOpen(true);
        const context = window.document.getElementById("context" + props.id);
        const newX = (e.clientX - 220 - 5);
        const newY = (e.clientY - 75 - 5);
        context.style.width = "100px";
        context.style.height = "fit-content";

        context.style.left = (e.clientX + context.offsetWidth < window.innerWidth ? newX : newX - context.offsetWidth) + "px";
        context.style.top = (e.clientY + context.offsetHeight < window.innerHeight ? newY : newY - context.offsetHeight) + "px";
    }

    const contextMenu = (props.type == "file" ? <ContextFile fileID={props.id} /> : <ContextFolder folderID={props.id} />);

    return (
        <>
            {props.noContext !== true &&
                <div id={"context" + props.id} className="contextMenu" onMouseLeave={() => { setIsContextOpen(false) }}>
                    {isContextOpen &&
                        contextMenu
                    }
                </div>
            }
            <div className="CustomIcon" draggable>
                <label className="dropzone"
                    htmlFor="icon"
                    onContextMenu={handleOnContextMenu}
                    onClick={handleOnClick}
                    onDragEnd={handelDragEnd}
                    onDragOver={handelDragOver}
                    onDrop={handelDrop} draggable />
                <div className="icon">
                    <div className="real" name={props.type}>
                        {icon}
                    </div>
                </div>
                <span className="name">{props.name}</span>
            </div>
        </>
    )

} 