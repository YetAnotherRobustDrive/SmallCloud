import React, { useState } from "react";
import { Link } from 'react-router-dom'
import logo_img from '../config/img/logo.png'
import configData from "../config/config.json"
import "../css/login.css"
import "../css/modal.css"
import ModalOk from "../component/modal/ModalOk";

export default function LoginPage() {

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
            <div className="buttons">
                <Link to='/register' className="link">회원가입</Link>
                <Link to='/auth' className="link">로그인</Link>
            </div>
            <Link to='/login-ask'>로그인에 문제가 있으신가요?</Link>
            <ModalOk>{"아이디 혹은 비밀번호를 확인해주세요!"}</ModalOk>
            <ModalOk>{"관리자에 의해 비활성화된 계정입니다.\n관리자에게 문의해주세요."}</ModalOk>
            <ModalOk>{"비밀번호가 초기화된 계정입니다.\n관리자에게 문의해주세요."}</ModalOk>
            <ModalOk>{"비밀번호 만료일 {}\n비밀번호를 변경해주세요."}</ModalOk>
            <ModalOk>{"비밀번호 만료일이 초과되었습니다.\n관리자에게 문의해주세요."}</ModalOk>
        </div>
    )
}