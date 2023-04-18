import React, { useState } from "react";
import { Link } from "react-router-dom";
import logo_img from "../config/img/logo.png"
import configData from "../config/config.json"
import "../css/login.css"
import "../css/modal.css"

export default function Signup() {

    const [name, setName] = useState();

    function getName() {
        setName(configData.name);
    }

    return (
        <div className="login" onLoad={getName}>
            <img src={logo_img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <input type="text" placeholder="ID" />
            <input type="password" placeholder="PW" />
            <input type="password" placeholder="PW Check" />
            <input type="text" placeholder="Nickname" />
            <div className="buttons">
                <button>가입 신청</button>
            </div>
            <Link to='/login-ask'>가입에 문제가 있으신가요?</Link>
        </div>
    )
}