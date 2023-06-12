import React, { useEffect, useState } from "react";
import { AiFillStar } from 'react-icons/ai';
import { BiShare } from 'react-icons/bi';
import { TbShare3 } from 'react-icons/tb';
import { FcFile, FcFolder } from 'react-icons/fc';
import { useNavigate } from "react-router-dom";
import '../../css/customIcon.css';
import ContextFolder from "../contextMenu/ContextFolder";
import jwtDecode from "jwt-decode";

export default function CustomIcon(props) {

    const [icon, setIcon] = useState(null);
    const [isContextOpen, setIsContextOpen] = useState(false);

    const navigate = useNavigate();

    const handleOnClick = (e) => {
        e.preventDefault();
        if (props.data.type === "folder") {
            if (props.isDeleted === true) {
                return;
            }
            navigate("/folder/" + props.data.id);
            return;
        }
        else {
            props.onClick();
            return;
        }
    }

    useEffect(() => {
        setIcon(
            props.data.type === "file" ? <FcFile /> : <FcFolder />
        )
    }, [])

    const handelDrop = (e) => {
        e.preventDefault();
        if (props.targetSetter === undefined) {
            return;
        }
        if (props.data.type === "folder") {
            props.targetSetter(props.data.id);
        }
    }

    const handelDragEnd = (e) => {
        e.preventDefault();
        if (props.sourceSetter === undefined) {
            return;
        }
        props.sourceSetter({ type: props.data.type, id: props.data.id });
    }
    const handelDragOver = (e) => {
        e.preventDefault();
    }

    const handleOnContextMenu = (e) => {
        e.preventDefault();
        e.stopPropagation();
        isContextOpen ? setIsContextOpen(false) : setIsContextOpen(true);
        const context = window.document.getElementById("context" + props.data.id);
        const newX = (e.clientX - 220 - 5);
        const newY = (e.clientY - 75 - 5);
        context.style.width = "100px";
        context.style.height = "fit-content";

        context.style.left = (e.clientX + context.offsetWidth < window.innerWidth ? newX : newX - context.offsetWidth) + "px";
        context.style.top = (e.clientY + context.offsetHeight < window.innerHeight ? newY : newY - context.offsetHeight) + "px";
    }

    const contextMenu = (props.data.type == "file" ? <></> : <ContextFolder folderID={props.data.id} isDeleted={props.data.isDeleted} isFavorite={props.data.isFavorite} />);

    return (
        <>
            {props.noContext !== true &&
                <div id={"context" + props.data.id} className="contextMenu" onMouseLeave={() => { setIsContextOpen(false) }}>
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
                {props.data.isFavorite &&
                    <div className="star"><AiFillStar /></div>
                }
                {props.data.isShareExist && (
                    <div className="shared"><BiShare /></div>
                )
                }
                <div className="icon">
                    <div className="real" name={props.data.type}>
                        {icon}
                    </div>
                </div>
                <span className="name">{props.data.name}</span>
            </div>
        </>
    )

} 