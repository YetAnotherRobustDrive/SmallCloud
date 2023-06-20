import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import EditableColumn from "../../component/mypage/EditableColumn";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import default_img from '../../img/defaultLogo.png';
import AdminUpdateLogo from "../../services/admin/AdminUpdateLogo";
import GetLogo from "../../services/config/GetLogo";
import GetName from "../../services/config/GetName";

export default function AdminLogoConfigPage() {    
    const [img, setImg] = useState(default_img);
    const [name, setName] = useState();
    const [isImgChanged, setIsImgChanged] = useState(false);

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
        const value = Object.fromEntries(inputData.entries());
        const res = await AdminUpdateLogo(value, isImgChanged);
        if (!res[0]) {
            SwalAlert("error", res[1]);
            return;
        }
        setName(value.name);
        SwalAlert("success", "로고가 변경되었습니다.", () => { window.location.reload(); })
    }

    const handleImgChange = (e) => {
        setIsImgChanged(true);
        const file = e.target.files[0];
        const reader = new FileReader();
        reader.readAsDataURL(file);

        return new Promise((resolve) => {
            reader.onload = () => {
                setImg(reader.result);
                resolve();
            };
        });
    }

    return (
        <>
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text="로고 & 이름 설정" />
                <form className="private-profile" onSubmit={handleSubmit}>
                    <div className="imgContainer">
                        <img src={img} alt="로고" />
                    </div>
                    <label htmlFor="file">로고 선택</label>
                    <input
                        onChange={handleImgChange}
                        className="imgInput"
                        type="file"
                        id="file"
                        name="location"
                        accept="image/*"
                        />
                    <EditableColumn
                        title="이름"
                        name="name"
                        value={name}
                    />
                    <button className="submitBtn" >저장하기</button>
                </form>

            </BodyFrame>
        </>
    )
}