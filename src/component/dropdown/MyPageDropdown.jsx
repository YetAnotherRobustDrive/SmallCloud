import React, { useEffect, useState } from "react";
import '../../css/dropdown.css'
import { BsPersonCircle } from "react-icons/bs"
import { Link } from "react-router-dom";

export default function MypageDropdown() {

    const options = [
        { value: "name_asc", label: "회원정보" },
        { value: "name_desc", label: <Link to='/upload'>업로드 목록</Link> },
        { value: "name_desc", label: <Link to='/download'>다운로드 목록</Link> },
        { value: "time_desc", label: "로그아웃" }
    ];

    const [isOpen, setIsOpen] = useState(false);

    const handleOptionClick = () => {
        setIsOpen(false);
    };

    return (
        <div className="dropdown">
            <div className="dropdown-header" onClick={() => setIsOpen(!isOpen)}>
                <BsPersonCircle />
                {isOpen && (
                    <ul className="dropdown-options" onMouseLeave={() => setIsOpen(false)}>
                        {options.map((option) => (
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