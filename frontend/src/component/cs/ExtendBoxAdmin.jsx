import React, { useState } from "react";
import { FiChevronUp, FiChevronDown } from 'react-icons/fi';
import '../../css/cs.css'

export default function ExtendBoxAdmin(props) {
    const [isOpen, setIsOpen] = useState(false);

    const handleClick = () => {
        setIsOpen(!isOpen);
    }
    return (//todo
        <div className="extendBox" key={props.key}>
            <div className="head" onClick={handleClick}>
                <span className="title">{props.title}</span>
                <div className="btn" >{isOpen ? <FiChevronUp /> : <FiChevronDown />}</div>
            </div>
            {isOpen &&
                <div className="quetionBody">
                    <div className="questionHeader">
                        <div>{props.writer}</div>
                        <div className="right">{props.contact}</div>
                    </div>
                    <div className="child">{props.children}</div>
                    <textarea className="child">test</textarea>
                    <button>등록하기</button>
                </div>
            }
        </div>
    )
}