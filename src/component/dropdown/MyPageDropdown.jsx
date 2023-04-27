import React, { useEffect, useState } from "react";
import '../../css/dropdown.css'

export default function MypageDropdown() {  

    const options = [
        { value: "name_asc", label: "이름 ↑" },
        { value: "name_desc", label: "이름 ↓" },
        { value: "time_asc", label: "시간 ↑" },
        { value: "time_desc", label: "시간 ↓" }
    ];

    const [isOpen, setIsOpen] = useState(false);
    const [selected, setSelected] = useState(options[0]);

    const handleOptionClick = (option) => {
        setSelected(option);
        setIsOpen(true);
    };

    return (
        <div className="dropdown">
            <div className="dropdown-header" onClick={() => setIsOpen(!isOpen)}>
                {!isOpen && (selected ? selected.label : "")}
            </div>
            {isOpen && (
                <div className="dropdown-options">
                    {options.map((option) => (
                        <div
                            key={option.value}
                            className="dropdown-option"
                            onClick={() => handleOptionClick(option)}
                        >
                            {option.label}
                        </div>
                    ))}
                </div>
            )}
        </div>
    )
}