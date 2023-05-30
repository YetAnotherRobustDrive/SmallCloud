import React, { useEffect, useState } from "react";
import '../../css/dropdown.css'
import { BsPersonCircle } from "react-icons/bs"
import { Link } from "react-router-dom";

export default function MypageDropdown() {

    const options = [
        { label: <Link to='/mypage'>마이페이지</Link>  },
        { label: <Link to='/logout'>로그아웃</Link> }
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
                        {options.map((option, idx) => (
                            <li
                                key={idx}
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