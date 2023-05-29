import React, { useState } from "react";
import '../css/bodyframe.css'
import ContextBody from "./contextMenu/ContextMenu";

export default function BodyFrame(props) {
    const hasContext = (props.hasContext === true ? true : false);
    const [isContextOpen, setIsContextOpen] = useState(false);
    const contextOptions = ["폴더 생성", "2222", "시간 ↑", "시간 ↓"];

    const handleContext = (e) => {
        e.preventDefault();
        if (!hasContext) return;

        const context = window.document.getElementById("context");
        const newX = (e.clientX - 220 - 5);
        const newY = (e.clientY - 75 - 5);

        context.style.left = (e.clientX + context.offsetWidth < window.innerWidth ? newX : newX - 100) + "px";
        context.style.top = (e.clientY + context.offsetHeight < window.innerHeight ? newY : newY - 100) + "px";
        isContextOpen ? setIsContextOpen(false) : setIsContextOpen(true);
    }

    return (
        <>
            <div className="bodyframe" onContextMenu={handleContext}>
                {hasContext &&
                    <div id="context" className="contextMenu" onMouseLeave={() => setIsContextOpen(false)}>
                        {isContextOpen &&
                            <ContextBody options={contextOptions} />
                        }
                    </div>
                }
                {props.children}
            </div>
        </>
    )
}