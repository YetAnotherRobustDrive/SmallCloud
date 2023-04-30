import React, { useState } from "react";
import { FiChevronUp, FiChevronDown } from 'react-icons/fi';
import '../../css/cs.css'

export default function ExtendBox(props) {
    const [isOpen, setIsOpen] = useState(false);

    const handleClick = () => {
        setIsOpen(!isOpen);
    }
    return (
        <div className="extendBox">
            <div className="head">
                <span className="title">{props.title}</span>
                <div className="btn" onClick={handleClick}>{isOpen ? <FiChevronUp /> : <FiChevronDown />}</div>
            </div>
            {isOpen &&
                <div className="child">{props.children}</div>
            }
        </div>
    )
}