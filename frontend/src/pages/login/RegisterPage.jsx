import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import logo_img from '../../config/img/logo.png'
import configData from "../../config/config.json"
import "../../css/login.css"
import "../../css/modal.css"
import ModalOk from "../../component/modal/ModalOk";

export default function RegisterPage() {

    const [name, setName] = useState();

    const [isChkWrong, setIsChkWrong] = useState(false);
    const [isEmpty, setIsEmpty] = useState(false);
    const [isExistUser, setIsExistUser] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const moveTo = useNavigate();

    const getName = () => {
        setName(configData.NAME);
    }

    const afterSuccess = () => {
        setIsSuccess(false);
        moveTo("/login");
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        if (inputData.get("name") === "" || inputData.get("id") === "" || inputData.get("password") === "" || inputData.get("password_chk") === "") {
            setIsEmpty(true);
            return;
        }
        else if (inputData.get("password_chk") !== inputData.get("password")) {
            setIsChkWrong(true);
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
            const res = await fetch(configData.API_SERVER + 'auth/register', model);
            if (!res.ok) {
                throw new Error('');//실패
            }
            setIsSuccess(true);//성공
        } catch (e) {
            setIsExistUser(true);//실패 후 처리
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

            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"입력하지 않은 값이 있습니다."}</ModalOk>}
            {isChkWrong && <ModalOk close={() => setIsChkWrong(false)}>{"비밀번호 확인이 일치하지 않습니다."}</ModalOk>}
            {isExistUser && <ModalOk close={() => setIsExistUser(false)}>{"이미 존재하는 유저입니다."}</ModalOk>}
            {isSuccess && <ModalOk close={afterSuccess}>{"가입되었습니다!!"}</ModalOk>}
        </form>
    )
}