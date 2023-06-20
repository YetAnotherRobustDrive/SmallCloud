import React, { useEffect, useState } from "react";
import "../../css/header.css";
import GetLogo from "../../services/config/GetLogo";
import GetName from "../../services/config/GetName";

export default function Logo(props) {

    const [name, setName] = useState();
    const [img, setImg] = useState();

    useEffect(() => {
        const getLogo = async () => {
            const res = await GetLogo();
            setImg(res);
        }
        const getName = async () => {
            const res = await GetName();
            setName(res);
        }
        getLogo();
        getName();
    }, [])


    return (
        <div className="logo">
            <img src={img} alt="LOGO" />
            <span>{name}</span>
        </div>
    )
}