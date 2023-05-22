import React, { useState } from "react";
import '../../css/dropdown.css'
import { BsBell } from "react-icons/bs"

export default function AlertDropdown() {

    const datas = [ //replace with fetched data
        { label: "1111" },
        { label: "2222" },
        { label: "시간 ↑" },
        { label: "시간 ↓" }
    ];

    const [isOpen, setIsOpen] = useState(false);

    const handleOptionClick = () => {
        setIsOpen(false);
    };

    return (
        <div className="dropdown">
            <div className="dropdown-header" onClick={() => setIsOpen(!isOpen)}>
                <BsBell />
                {isOpen && (
                    <ul className="dropdown-options" onMouseLeave={() => setIsOpen(false)}>
                        {datas.map((option) => (
                            <li
                                key={option.value}
                                className="dropdown-option"
                                onClick={() => handleOptionClick(option)}
                            >
                                {option.label}
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    )
}