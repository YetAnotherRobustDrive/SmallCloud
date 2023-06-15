import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import ModalGetPW from "../../component/modal/ModalGetPW";
import EditableColumn from "../../component/mypage/EditableColumn";
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import '../../css/mypage.css';
import RefreshToken from "../../services/token/RefreshToken";
import GetUserInfo from "../../services/user/GetUserInfo";
import UpdateUserInfo from "../../services/user/UpdateUserInfo";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import BodyHeader from "../../component/main/BodyHeader"
import SwalConfirm from "../../component/swal/SwalConfirm";
import DeregisterUser from "../../services/user/DeregisterUser";

export default function PrivatePage() {
    const [img, setImg] = useState(null);
    const [username, setUsername] = useState(null);
    const [nickname, setNickname] = useState(null);
    const [group, setGroup] = useState("소속이 없습니다.");
    const [joined, setJoined] = useState(null);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isImgChanged, setIsImgChanged] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const getUserInfo = async () => {
            await RefreshToken();

            const res = await GetUserInfo();
            if (!res[0]) {
                SwalAlert("error", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", navigate("/"));
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
            SwalError(res[1]);
            return;
        }
        SwalAlert("success", "회원정보가 수정되었습니다.", navigate("/mypage"));
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

    const handleDeregister = async (e) => {
        e.preventDefault();
        SwalConfirm(
            "정말로 회원 탈퇴를 하시겠습니까?",
            async () => {
                const res = await DeregisterUser({ isDeregister: true }, false);
                if (!res[0]) {
                    SwalError(res[1]);
                    return;
                }
                SwalAlert("success", "회원 탈퇴가 완료되었습니다.", navigate("/"));
            },
            () => { return; }
        );
    }

    const handleChangeServer = async (e) => {
        e.preventDefault();
        SwalConfirm(
            "서버를 변경하시겠습니까?",
            async () => {
                localStorage.clear();
                SwalAlert("success", "서버 설정이 초기화되었습니다.", () => { window.location.reload(); })
            },
            () => { return; }
        );
    }

    return (
        <>
            {isModalOpen &&
                <ModalGetPW
                    title={"비밀번호 변경"}
                    isOpen={isModalOpen}
                    after={() => setIsModalOpen(false)}
                />}
            <Header />
            <SidebarMypage />
            <BodyFrame>
                <BodyHeader text="회원정보" />
                <form className="private-profile" onSubmit={handleSubmit}>
                    <img src={img} alt="프로필 이미지" />
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
                    <div className="addtional-func">
                        <button className="submitBtn" onClick={handleDeregister} >회원 탈퇴</button>
                        <button className="submitBtn" onClick={handleChangeServer} >서버 변경</button>
                    </div>
                </form>

            </BodyFrame>
        </>
    )
    //회원 탈퇴 기능 추가
    //서버 변경 기능 추가
}
