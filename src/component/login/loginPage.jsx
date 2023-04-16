import React, { useState } from "react";
import logo_img from "../../config/img/logo.png"
import configData from "../../config/config.json"
import "../../css/login.css"
import ModalOk from "../modal/modalOk";
import ModalConfirmRemove from "../modal/modalConfirmRemove";
import ModalCheckPW from "../modal/modalCheckPw";
import "../../css/modal.css"
import ModalShare from "../modal/modalShare";

export default function LoginPage() {

    const [name, setName] = useState();

    function getName() {
        setName(configData.name);
    }

    return (
        <div className="login" onLoad={getName}>
            <img src={logo_img} alt="LOGO" />
            <span >{name}</span>
            <input type="text" placeholder="ID" />
            <input type="password" placeholder="PW" />
            <div className="buttons">
                <button>회원가입</button>
                <button>로그인</button>
            </div>
            <a href="">로그인에 문제가 있으신가요?</a>
        </div>
    )
    /*
            <ModalOk text="성공적으로 전송되었습니다!"/>
            <ModalConfirmRemove/>
            <ModalCheckPW/>
            <ModalShare/>
    */
}