import React, {useState} from "react";
import logo_img from  "../../config/img/logo.png"
import configData from "../../config/config.json"
import "../../css/login.css"

export default function LoginPage() {

    const [name, setName] = useState();

    function getName(){
        setName(configData.name);
    }

    return (
        <div className="login" onLoad={getName}>
            <img src={logo_img} alt="LOGO" />
            <span>{name}</span>
            <input type="text" placeholder="Your ID"/>
            <input type="text" placeholder="Your PW"/>
            <div className="buttons">
                <input type="button" value="회원가입"/>
                <input type="button" value="로그인"/>
            </div>
            <a href="">아 하기 싫다~</a>
        </div>
    )
}