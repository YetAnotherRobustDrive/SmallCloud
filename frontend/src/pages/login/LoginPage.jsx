import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from 'react-router-dom';
import ModalOk from "../../component/modal/ModalOk";
import configData from "../../config/config.json";
import logo_img from '../../config/img/logo.png';
import "../../css/login.css";
import "../../css/modal.css";
import IsAdminToken from "../../services/token/IsAdminToken";
import { asyncCheckAdmin } from "../../slice/TokenSlice";
import { setIsLoggedIn } from "../../slice/UserSlice";
import ThrowPingAs from "../../services/log/ThrowPingAs";
import ErrorHandler from "../../component/main/ErrorHandler";

export default function LoginPage() {

    const [name, setName] = useState();
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [loginInfo, setLoginInfo] = useState({isSuccess: false, id: ''});
    const [message, setMessage] = useState("로그인 에러");
    const navigate = useNavigate();

    const dispath = useDispatch();

    function getName() {
        setName(configData.NAME);
    }

    const afterSuccess = () => {
        dispath(setIsLoggedIn(loginInfo.id));
        setLoginInfo({isSuccess: false, id: ''});
        navigate('/');
    }
    
    const dispatch = useDispatch();
    const checkAdmin = async () => {
        const isAdmin = await IsAdminToken();
        if (!isAdmin) { //fail
            return;
        }
        dispatch(asyncCheckAdmin()); 
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        let model = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(value),
        };

        if (inputData.get("id") === "" || inputData.get("password") === "") {
            setIsEmpty(true);
            return;
        }

        try {
            const res = await fetch(configData.API_SERVER + 'auth/login', model);
            const data = await res.json();
            if (!res.ok) {
                ThrowPingAs("login/" + value.id + "/fail");
                throw data;
            }
            ThrowPingAs("login/" + value.id + "/success");
            localStorage.setItem("accessToken", data.accessToken); //성공
            localStorage.setItem("refreshToken", data.refreshToken);
            await checkAdmin();//check admin        
            
            setLoginInfo({isSuccess: true, id: inputData.get("id")});
            return;
        } catch (e) {
            ErrorHandler(e);
        }
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
            <Link to='/login/ask'>로그인에 문제가 있으신가요?</Link>

            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"입력하지 않은 값이 있습니다."}</ModalOk>}
            {loginInfo.isSuccess && <ModalOk close={afterSuccess}>{"로그인 성공"}</ModalOk>}
        </form>
    )
}