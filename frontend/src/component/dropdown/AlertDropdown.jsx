import React, { useState } from "react";
import '../../css/dropdown.css'
import { BsBell } from "react-icons/bs"
import { TbDots } from "react-icons/tb";

export default function AlertDropdown() {

    const datas = [ //replace with fetched data
        { label: "개인정보처", date: "2021-05-01" },
        { label: "222222222222222222222222", date: "2021-05-01" },
        { label: "222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222", date: "2021-05-01" },
        { label: "222222222222222222222222", date: "2021-05-01" },
        { label: "222222222222222222222222", date: "2021-05-01" },
    ];

    const [isOpen, setIsOpen] = useState(false);

    const handleOptionClick = (e) => {  
        e.preventDefault();
        e.stopPropagation();
    };

    const handleRemoveClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        setIsOpen(false);
    };

    return (
        <div className="dropdown">
            <div className="dropdown-header" onClick={() => setIsOpen(!isOpen)}>
                <BsBell />
                {isOpen && (
                    <ul className="dropdown-options">
                        {datas.map((option) => (
                            <li
                                key={option.value}
                                className="dropdown-option"
                                onClick={handleOptionClick}
                            >
                                <p><span className="content" role="textbox">
                                    {option.label}
                                </span></p>
                                <div className="date">
                                    {option.date}
                                    <div className="close" onClick={handleRemoveClick}>읽음</div>
                                </div>
                            </li>
                        ))}
                        <div className="more"><TbDots/></div>
                    </ul>
                )}
            </div>
        </div>
    )
}