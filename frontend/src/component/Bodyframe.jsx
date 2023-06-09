import React, { useEffect, useState } from "react";
import '../css/bodyframe.css'
import ContextBody from "./contextMenu/ContextBody";

export default function BodyFrame(props) {
    const hasContext = (props.hasContext === true ? true : false);
    const [isContextOpen, setIsContextOpen] = useState(false);

    useEffect(() => {
        if (!hasContext) return;
        const context = window.document.getElementById("context");

        context.style.width = "0";
        context.style.height = "0";

    }, [])
    const handleContext = (e) => {
        e.preventDefault();
        if (!hasContext) return;

        const context = window.document.getElementById("context");
        const newX = (e.clientX - 220 - 5);
        const newY = (e.clientY - 75 - 5);
        context.style.width = "100px";
        context.style.height = "fit-content";

        context.style.left = (e.clientX + context.offsetWidth < window.innerWidth ? newX : newX - context.offsetWidth) + "px";
        context.style.top = (e.clientY + context.offsetHeight < window.innerHeight ? newY : newY - context.offsetHeight) + "px";
        isContextOpen ? setIsContextOpen(false) : setIsContextOpen(true);
    }

    return (
        <>
            <div className={"bodyframe" + (props.innerWidth <= 786 ? " bodyframeWithoutSide" : "")} onContextMenu={handleContext}>
                {hasContext &&
                    <div id="context" className="contextMenu" onMouseLeave={() => setIsContextOpen(false)} >
                        {isContextOpen && <ContextBody />}
                    </div>
                }
                {props.children}
            </div>
        </>
    )
}