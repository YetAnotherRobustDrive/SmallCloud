import React, { useState } from "react";
import SwalError from "../../component/swal/SwalError";
import logo_img from '../../config/img/logo.png';
import "../../css/login.css";
import "../../css/modal.css";

export default function ConfigPage() {

    const [name, setName] = useState();ㅒ

    const handleSubmit = async (e) => {
        e.preventDefault();
        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        SwalError(value)

    }
    return (
        <form className="login" onLoad={getName} onSubmit={handleSubmit}>
            <img src={logo_img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <input name='id' type="text" placeholder="ID" autoFocus />
            <input name='password' type="password" placeholder="PW" />
            <div className="buttons">
                <button className="link" >설정완료</button>
            </div>
        </form>
    )
}