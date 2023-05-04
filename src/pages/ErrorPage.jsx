import React, { useState } from "react";
import logo_img from '../config/img/logo.png'
import configData from "../config/config.json"
import "../css/error.css"
import { Link } from "react-router-dom";

export default function ErrorPage() {

    const [name, setName] = useState();

    function getName() {
        setName(configData.NAME);
    }

    return (
        <div className="error" onLoad={getName}>
            <img src={logo_img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <span className="errorspan">존재하지 않는 페이지입니다.</span>
            <Link className="backhome" to="/">홈으로</Link>
        </div>
    )
}