import React, { useEffect, useState } from "react";
import '../../css/dropdown.css'
import { BsBell } from "react-icons/bs"
import GetFiveAlarm from "../../services/alarm/GetFiveAlarm";
import PostDeleteAlarm from "../../services/alarm/PostDeleteAlarm";
import SwalAlert from "../swal/SwalAlert";
import { useSelector } from "react-redux";
export default function AlertDropdown() {

    const [alarms, setAlarms] = useState([]);
    const [count, setCount] = useState(0);
    const isAdmin = useSelector(state => state.token.isAdmin);

    useEffect(() => {
        const getAlarm = async () => {
            const res = await GetFiveAlarm();
            if (!res[0]) {
                SwalAlert("error", res[1]);
                return;
            }
            setAlarms(res[1].notificationDtoList);
            setCount(res[1].count);
        }
        if (!isAdmin) {
            getAlarm();
        }
    }, [count]);

    const [isOpen, setIsOpen] = useState(false);

    const handleOptionClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
    };

    const handleRemoveClick = (e) => {
        e.preventDefault();
        e.stopPropagation();
        const rmAlarm = async () => {
            const res = await PostDeleteAlarm(e.target.id);
            if (!res[0]) {
                SwalAlert("error", res[1]);
                return;
            }
            setCount(count - 1);
        }
        rmAlarm();
    };

    return (
        <div className="dropdown">
            <div className="dropdown-header" onClick={() => setIsOpen(!isOpen)}>
                {count !== 0 &&
                    <span className="count">{count > 99 ? "99+" : count}</span>
                }
                <BsBell />
                {isOpen && (
                    <ul className="dropdown-options">
                        {alarms.map((option) => (
                            <li
                                key={option.id}
                                className="dropdown-option"
                                onClick={handleOptionClick}
                            >
                                <p><span className="content" role="textbox">
                                    {option.label}
                                </span></p>
                                <div className="date">
                                    {option.date}
                                    <div id={option.id} className="close" onClick={handleRemoveClick}>읽음</div>
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