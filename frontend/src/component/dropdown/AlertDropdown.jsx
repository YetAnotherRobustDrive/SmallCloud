import React, { useEffect, useState } from "react";
import '../../css/dropdown.css'
import { BsBell } from "react-icons/bs"
export default function AlertDropdown() {

    const [alarms, setAlarms] = useState([]);
    const [count, setCount] = useState(1000);

    useEffect(() => {

        setAlarms([ //replace with fetched data
            { label: "알림테스트", date: "2021-05-01" },
            { label: "222222222222222222222222", date: "2021-05-01" },
            { label: "222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222", date: "2021-05-01" },
            { label: "222222222222222222222222", date: "2021-05-01" },
            { label: "222222222222222222222222", date: "2021-05-01" },
        ]);
    }, []);

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
                <span className="count">{count > 99 ? "99+" : count}</span>
                <BsBell />
                {isOpen && (
                    <ul className="dropdown-options">
                        {alarms.map((option) => (
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
                        <div className="more" onClick={handleOptionClick}>최근 5개의 알림만 표시됩니다.</div>
                    </ul>
                )}
            </div>
        </div>
    )
}