import React, { useState } from "react";
import { Link } from "react-router-dom";
import logo_img from "../config/img/logo.png"
import configData from "../config/config.json"
import "../css/login.css"
import "../css/modal.css"
import ModalOk from "../component/modal/ModalOk";

export default function Signup() {

    const [name, setName] = useState();
    const [isChkWrong, setIsChkWrong] = useState(false);
    const [isEmpty, setIsEmpty] = useState(false);

    const getName = () => {
        setName(configData.NAME);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        let model = {
            method: "POST",
            body: JSON.stringify(value),
            headers: {
                "Content-Type": "application/json",
            },
        };
        if (inputData.get("name") == "" || inputData.get("id") == "" || inputData.get("password") == "" || inputData.get("password_chk") == "") {
            setIsEmpty(true);
            return;
        }
        else if (inputData.get("password_chk") != inputData.get("password")) {
            setIsChkWrong(true);
            return;
        }
        console.log(model);
        inputData.delete("password_chk");
        fetch(configData.API_SERVER + '/auth/register', model)
        .then(res => res.json())
        .then(data => console.log(data));

    }

    return (
        <form className="login" onLoad={getName} onSubmit={handleSubmit}>
            <img src={logo_img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <input name="id" type="text" placeholder="ID" />
            <input name="password" type="password" placeholder="PW" />
            <input name="password_chk" type="password" placeholder="PW Check" />
            <input name="name" type="text" placeholder="Nickname" />
            <div className="buttons">
                <button>가입 신청</button>
            </div>
            <Link to='/login-ask'>가입에 문제가 있으신가요?</Link>
            {/* <ModalConfirmRemove></ModalConfirmRemove>
            <ModalCheckPw></ModalCheckPw> */}
            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"입력하지 않은 값이 있습니다."}</ModalOk>}
            {isChkWrong && <ModalOk close={() => setIsChkWrong(false)}>{"비밀번호 확인이 일치하지 않습니다."}</ModalOk>}
        </form>
    )
}