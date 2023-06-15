import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import configData from "../../config/config.json"
import logo_img from '../../config/img/logo.png';
import "../../css/login.css";
import "../../css/modal.css";

export default function RegisterPage() {

    const [name, setName] = useState();

    const moveTo = useNavigate();

    const getName = () => {
        setName(configData.NAME);
    }

    const afterSuccess = () => {
        moveTo("/login");
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        if (inputData.get("name") === "" || inputData.get("id") === "" || inputData.get("password") === "" || inputData.get("password_chk") === "") {
            SwalError("모든 항목을 입력해주세요.");
            return;
        }
        else if (inputData.get("password_chk") !== inputData.get("password")) {
            SwalError("비밀번호가 일치하지 않습니다.");
            return;
        }
        inputData.delete("password_chk");

        let model = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(value),
        };

        try {
            const res = await fetch(localStorage.getItem("API_SERVER") + 'auth/register', model);
            if (!res.ok) {
                SwalError("회원가입에 실패하였습니다.");
                return;
            }
            SwalAlert("success", "회원가입이 완료되었습니다.", afterSuccess);
        } catch (e) {
            if (e.message !== undefined) {
                SwalError(e.message);
            }
            else {
                SwalError("회원가입에 실패하였습니다.");
            }
        }
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
                <Link to='/login' className="link">로그인</Link>
                <button className="link" >가입 신청</button>
            </div>
            <Link to='/login/ask'>가입에 문제가 있으신가요?</Link>
        </form>
    )
}