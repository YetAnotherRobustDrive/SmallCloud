import React, { useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import ModalOk from "../../component/modal/ModalOk";
import configData from "../../config/config.json";
import logo_img from '../../config/img/logo.png';
import "../../css/login.css";
import "../../css/modal.css";

export default function ConfigPage() {

    const [name, setName] = useState();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        alert(value);

    }
    return (
        <form className="login" onLoad={getName} onSubmit={handleSubmit}>
            <img src={logo_img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <input name='id' type="text" placeholder="ID" autoFocus />
            <input name='password' type="password" placeholder="PW" />
            <div className="buttons">
                <Link to='/register' className="link">회원가입</Link>
                <button className="link" >로그인</button>
            </div>
        </form>
    )
}