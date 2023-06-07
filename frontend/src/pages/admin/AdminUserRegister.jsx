import React, { useState } from "react";
import BodyFrame from '../../component/Bodyframe';
import Header from '../../component/header/Header';
import BodyHeader from "../../component/main/BodyHeader";
import ModalOk from "../../component/modal/ModalOk";
import SidebarAdmin from '../../component/sidebar/SidebarAdmin';
import configData from "../../config/config.json";
import logo_img from '../../config/img/logo.png';
import "../../css/login.css";
import "../../css/modal.css";
import AdminRegisterUser from "../../services/admin/AdminRegisterUser";

export default function AdminUserRegister() {

    const [name, setName] = useState();

    const [isChkWrong, setIsChkWrong] = useState(false);
    const [isEmpty, setIsEmpty] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [message, setMessage] = useState("로그인 에러");

    const getName = () => {
        setName(configData.NAME);
    }

    const afterSuccess = () => {
        setIsSuccess(false);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        if (inputData.get("name") === "" || inputData.get("id") === "" || inputData.get("password") === "" || inputData.get("password_chk") === "") {
            setIsEmpty(true);
            return;
        }
        else if (inputData.get("password_chk") !== inputData.get("password")) {
            setIsChkWrong(true);
            return;
        }
        inputData.delete("password_chk");
        const value = Object.fromEntries(inputData.entries());
        if (value["expiredDate"] !== undefined) {
            value["expiredDate"] = value["expiredDate"] + "T00:00:00.000000";            ㅇ
        }

        const res = await AdminRegisterUser(value);

        if (!res[0]) {
            setIsFail(true);
            setMessage(res[1]);
            return;
        }
        setIsSuccess(true);
    }

    const handleActivate = (e) => {
        const date = document.getElementsByName("expiredDate")[0];
        if (e.target.checked) {
            date.disabled = false;
        }
        else {
            date.disabled = true;
        }
    }

    return (
        <>
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text="신규 사용자 등록" />
                <form className="login" onLoad={getName} onSubmit={handleSubmit}>
                    <img src={logo_img} alt="LOGO" />
                    <span className="namespan">{name}</span>
                    <input name="id" type="text" placeholder="ID" />
                    <input name="password" type="password" placeholder="PW" />
                    <input name="password_chk" type="password" placeholder="PW Check" />
                    <input name="name" type="text" placeholder="Nickname" />
                    <div className="expire">
                        <div>
                            <p/>
                            <span>계정 만료일 설정</span>
                            <input type="checkbox" onClick={handleActivate} />
                        </div>
                        <input name="expiredDate" type="date" placeholder="만료일" style={{width:"267px"}} disabled />
                    </div>
                    <div className="buttons">
                        <button type="" className="link">등록</button>
                    </div>

                    {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
                    {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"입력하지 않은 값이 있습니다."}</ModalOk>}
                    {isChkWrong && <ModalOk close={() => setIsChkWrong(false)}>{"비밀번호 확인이 일치하지 않습니다."}</ModalOk>}
                    {isSuccess && <ModalOk close={afterSuccess}>{"가입되었습니다!!"}</ModalOk>}
                </form>
            </BodyFrame>
        </>
    )
}

