import React, { useEffect, useState } from "react";
import { FcFile, FcFolder } from 'react-icons/fc';
import { useNavigate } from "react-router-dom";
import '../../css/customIcon.css';

export default function CustomIcon(props) {

    const [icon, setIcon] = useState(null);
    const [left, setLeft] = useState(null);
    const [right, setRight] = useState(null);
    const navigate = useNavigate();

    const handleOnClick = () => {
        if (props.type == "file") {
            props.onClick();
            return;
        }
        else {
            navigate("files/" + props.id);
            return;
        }
    }

    useEffect(() => {
        setIcon(
            props.type == "file" ? <FcFile /> : <FcFolder />
        )
        setLeft(() => {
            switch (props.stage) {
                case "DRAFT":
                    return "초안";
                case "EXPIRED":
                    return "만료";
                case "FINAL":
                    return "최종";
                default:
                    return "";
            }
        })
        setRight(() => {
            switch (props.secu) {
                case "CONFIDENTIAL":
                    return "기밀";
                case "PUBLIC":
                    return "공개";
                case "SENSITIVE":
                    return "민감";
                default:
                    return "";
            }
        })
    }, [])
    return (
        <div className="CustomIcon" onClick={handleOnClick}>
            <div className="icon">
                {icon}
                <div className="labels">
                    <div className="left">{left}</div>
                    <div className="right">{right}</div>
                </div>
            </div>
            <span className="name">{props.name}</span>
        </div>
    )

} 