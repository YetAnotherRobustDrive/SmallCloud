import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import ModalGetPW from "../../component/modal/ModalGetPW";
import ModalOk from '../../component/modal/ModalOk';
import EditableColumn from "../../component/mypage/EditableColumn";
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import '../../css/mypage.css';
import RefreshToken from "../../services/token/RefreshToken";
import GetUserInfo from "../../services/user/GetUserInfo";
import UpdateUserInfo from "../../services/user/UpdateUserInfo";

export default function PrivatePage() {
    const [img, setImg] = useState(null);
    const [username, setUsername] = useState(null);
    const [nickname, setNickname] = useState(null);
    const [group, setGroup] = useState("소속이 없습니다.");
    const [joined, setJoined] = useState(null);

    const [isFail, setIsFail] = useState(false);
    const [isFetchFail, setIsFetchFail] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [message, setMessage] = useState(false);
    const [isImgChanged, setIsImgChanged] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const getUserInfo = async () => {
            await RefreshToken();

            const res = await GetUserInfo();
            if (!res[0]) {
                setIsFetchFail(true);
            }

            //성공 후 처리
            setUsername(res[1].username);
            setNickname(res[1].nickname);
            const joinDate = new Date(res[1].joinedDate);
            const year = ("" + joinDate.getFullYear()).slice(2);
            const month = ("0" + (1 + joinDate.getMonth())).slice(-2);
            const day = ("0" + joinDate.getDate()).slice(-2);
            const hour = ("0" + joinDate.getHours()).slice(-2);
            const min = ("0" + joinDate.getMinutes()).slice(-2);
            setJoined(year + "-" + month + "-" + day + ' ' + hour + ':' + min);
            if (res[1].group !== null)
                setGroup(res[1].group);

            setImg(res[2]);
        }
        getUserInfo();
    }, [])


    const handleSubmit = async (e) => {
        e.preventDefault();
        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        const res = await UpdateUserInfo(value, isImgChanged);
        if (!res[0]) {
            setIsFail(true);
            setMessage(res[1] === undefined ? "잘못된 값이 있습니다." : res[1]);
            return;
        }
        setIsSuccess(true);
    }

    const handleChangePW = async (e) => {
        e.preventDefault();
        setIsModalOpen(true);
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
            {isModalOpen &&
                <ModalGetPW
                    title={"비밀번호 변경"}
                    isOpen={isModalOpen}
                    after={() => setIsModalOpen(false)}
                />}
            {isSuccess && <ModalOk close={() => { window.location.reload() }}>{"변경되었습니다."}</ModalOk>}
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isFetchFail && <ModalOk close={() => { setIsFail(false); navigate('/'); }}>{"일시적인 오류가 발생했습니다."}</ModalOk>}
            <Header />
            <SidebarMypage />
            <BodyFrame>
                <form className="private-profile" onSubmit={handleSubmit}>
                    <img src={img} alt="프로필 이미지"/>
                    <label htmlFor="file">프로필 변경하기</label>
                    <input
                        onChange={handleImgChange}
                        className="imgInput"
                        type="file"
                        id="file"
                        name="location"
                        accept="image/*" />
                    <EditableColumn
                        title="ID"
                        name="username"
                        value={username}
                    />
                    <EditableColumn
                        title="NAME"
                        name="nickname"
                        value={nickname}
                    />
                    <EditableColumn
                        title="GROUP"
                        value={group}
                        disabled={true}
                    />
                    <EditableColumn
                        title="PW"
                        value="****"
                        disabled={true}
                        onClick={handleChangePW}
                    />
                    <EditableColumn
                        title="Joined"
                        value={joined}
                        disabled={true}
                    />
                    <button className="submitBtn" >저장하기</button>
                </form>
            </BodyFrame>
        </>
    )
}
