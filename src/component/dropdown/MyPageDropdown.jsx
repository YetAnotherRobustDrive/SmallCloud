import React, { useEffect, useState } from "react";
import '../../css/dropdown.css'
import { BsPersonCircle } from "react-icons/bs"
import { Link } from "react-router-dom";

export default function MypageDropdown() {

    const options = [
        { id: 1, label: <Link to='/mypage'>마이페이지</Link>  },
        { id: 2, label: <Link to='/upload'>업로드 목록</Link> },
        { id: 3, label: <Link to='/download'>다운로드 목록</Link> },
        { id: 4, label: <Link to='/logout'>로그아웃</Link> }
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
                                key={option.id}
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