import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import "../../css/login.css";
import "../../css/modal.css";
import GetConfig from "../../services/config/GetConfig";
import GetLogo from "../../services/config/GetLogo";
import GetName from "../../services/config/GetName";

export default function RegisterPage() {

    const [name, setName] = useState();
    const [img, setImg] = useState();

    const moveTo = useNavigate();

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

    const afterSuccess = () => {
        moveTo("/login");
    }

    const handleSubmit = async (e) => {
        e.preventDefault();


        const inputData = new FormData(e.target);
        if (inputData.get("name") === "" || inputData.get("id") === "" || inputData.get("password") === "" || inputData.get("password_chk") === "") {
            SwalError("모든 항목을 입력해주세요.");
            return;
        }
        else if (inputData.get("password_chk") !== inputData.get("password")) {
            SwalError("비밀번호가 일치하지 않습니다.");
            return;
        }
        inputData.delete("password_chk");
        const value = Object.fromEntries(inputData.entries());

        const configRes = await GetConfig("201");
        if (!configRes[0]) {
            SwalError(configRes[1]);
            return;
        }
        const isCombinationNeeded = (configRes[1] === "true");
        if (isCombinationNeeded) {
            if (!value.password.match(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9])/)) {
                SwalError("비밀번호는 영문+숫자+특수문자 조합으로 입력해주세요.");
                return;
            }
        }

        const configRes2 = await GetConfig("202");
        if (!configRes2[0]) {
            SwalError(configRes2[1]);
            return;
        }
        const minimumLength = parseInt(configRes2[1]);
        if (minimumLength > value.password.length) {
            SwalError("비밀번호는 " + minimumLength + "자 이상으로 입력해주세요.");
            return;
        }


        const model = {
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
        <form className="login" onSubmit={handleSubmit}>
            <img src={img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <input name="id" type="text" placeholder="ID" />
            <input name="password" type="password" placeholder="PW" />
            <input name="password_chk" type="password" placeholder="PW Check" />
            <input name="name" type="text" placeholder="Nickname" />
            <div className="buttons">
                <button className="link" >회원가입</button>
                <Link to='/login' className="link">로그인</Link>
            </div>
            <Link to='/login/ask'>가입에 문제가 있으신가요?</Link>
        </form>
    )
}