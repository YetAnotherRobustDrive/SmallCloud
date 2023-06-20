import React, { useEffect, useState } from "react";
import BodyFrame from '../../component/Bodyframe';
import Header from '../../component/header/Header';
import BodyHeader from "../../component/main/BodyHeader";
import SidebarAdmin from '../../component/sidebar/SidebarAdmin';
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import "../../css/login.css";
import "../../css/modal.css";
import AdminRegisterUser from "../../services/admin/AdminRegisterUser";
import GetLogo from "../../services/config/GetLogo";
import GetName from "../../services/config/GetName";

export default function AdminUserRegister() {

    const [name, setName] = useState();
    const [img, setImg] = useState();

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
        if (value["expiredDate"] !== undefined) {
            value["expiredDate"] = value["expiredDate"] + "T23:59:59.000000";
        }

        const res = await AdminRegisterUser(value);
        if (!res[0]) {
            SwalError(res[1]);
            return;
        }
        SwalAlert("success", "사용자가 등록되었습니다.", () => window.location.reload());
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
                <form className="login" onSubmit={handleSubmit}>
                    <img src={img} alt="LOGO" />
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
                </form>
            </BodyFrame>
        </>
    )
}

