import React, { useEffect, useState } from "react";
import { TbEdit } from "react-icons/tb";
import BodyFrame from "../../component/Bodyframe";
import RuleBox from "../../component/admin/ruleBox";
import TitledBox from "../../component/admin/titledBox";
import ToggleBtn from "../../component/admin/toggleBtn";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalGetString from "../../component/modal/ModalGetString";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import '../../css/admin.css';
import default_profile_img from '../../img/defalutProfile.png';
import AdminActivateUser from "../../services/admin/AdminActivateUser";
import AdminDeactivateUser from "../../services/admin/AdminDeactivateUser";
import AdminExpireUser from "../../services/admin/AdminExpireUser";
import AdminGetUserInfo from "../../services/admin/AdminGetUserInfo";
import AdminInitUserPw from "../../services/admin/AdminInitUserPw";
import GetSearchUser from "../../services/user/GetSearchUser";
import ModalGroupConfig from "../../component/modal/ModalGroupConfig";
import AdminGroupAdd from "../../services/admin/AdminGroupAdd";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";


export default function AdminUserCtrlPage() {

    const [img, setImg] = useState(default_profile_img);
    const [user, setUser] = useState(null);
    const [joined, setJoined] = useState(null);
    const [searched, setSearched] = useState([]);
    const [newPw, setNewPw] = useState("");
    const [newGroup, setNewGroup] = useState("");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isGroupModalOpen, setIsGroupModalOpen] = useState(false);

    useEffect(() => {
        const initPw = async () => {
            if (newPw === "") {
                return;
            }
            const res = await AdminInitUserPw(user.username, newPw);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            SwalAlert("success", "비밀번호 초기화에 성공했습니다.");
            setNewPw("");
        }
        initPw();
    }, [newPw]);

    useEffect(() => {
        const setGroup = async () => {
            if (newGroup === "") {
                return;
            }
            else if (newGroup === "없음") {
                setNewGroup("");
                setUser({
                    ...user,
                    groupName: newGroup,
                })
                return;
            }
            const res = await AdminGroupAdd(newGroup, user.username);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            SwalAlert("success", "그룹 추가에 성공했습니다.");
            setNewGroup("");
            setUser({
                ...user,
                groupName: newGroup,
            })
        }
        setGroup();
    }, [newGroup]);

    const handleDeactivate = async () => {
        const res = await AdminDeactivateUser(user.username);
        if (!res[0]) {
            SwalError(res[1]);
            return;
        }
        SwalAlert("success", "사용자 계정 비활성화에 성공했습니다.");
        setUser({
            ...user,
            locked: true,
        })
    }

    const handleActivate = async () => {
        const res = await AdminActivateUser(user.username);
        if (!res[0]) {
            SwalError(res[1]);
            return;
        }
        SwalAlert("success", "사용자 계정 활성화에 성공했습니다.");
        setUser({
            ...user,
            locked: false,
        })
    }

    const handlePwInit = async () => {
        setIsModalOpen(true);
    }

    const handleUserSelect = async (e) => {
        if (e.target.firstChild.innerHTML === undefined) {
            return;
        }
        const res = await AdminGetUserInfo(e.target.firstChild.innerHTML);
        document.getElementById("searchUser").value = "";
        setSearched([]);
        if (!res[0]) {
            SwalError(res[1]);
            return;
        }
        setUser(res[1]);
        setImg(res[2]);
        const joinDate = new Date(res[1].joinedDate);
        const year = ("" + joinDate.getFullYear()).slice(2);
        const month = ("0" + (1 + joinDate.getMonth())).slice(-2);
        const day = ("0" + joinDate.getDate()).slice(-2);
        const hour = ("0" + joinDate.getHours()).slice(-2);
        const min = ("0" + joinDate.getMinutes()).slice(-2);
        setJoined(year + "-" + month + "-" + day + ' ' + hour + ':' + min);
    }

    const handleKeyDown = (e) => {
        if (e.key === 'Escape') {
            e.preventDefault();
            setSearched([]);
            e.target.value = "";
        }
    }

    const handleChange = async (e) => {
        if (e.target.value !== "") {
            const res = await GetSearchUser(e.target.value);
            if (!res[0]) {
                return;
            }
            const user = res[1].map((d) => {
                return {
                    "name": d,
                    "type": "MEMBER",
                }
            })
            setSearched(user);
        }
        else {
            setSearched([]);
        }
    }

    const handleExpire = async (e) => {
        e.preventDefault();
        const inputData = new FormData(e.target);
        if (inputData.get("newExpireDate") === "" || inputData.get("newExpireDate") === null) {
            SwalAlert("error", "만료일을 입력해주세요.");
            return;
        }
        const value = {
            "username": user.username,
            "expireDate": inputData.get("newExpireDate") + "T23:59:59.000000",
        }
        const res = await AdminExpireUser(value);
        if (!res[0]) {
            SwalError(res[1]);
            return;
        }
        SwalAlert("success", "만료일 설정에 성공했습니다.");
    }

    return (
        <>
            {isGroupModalOpen &&
                <ModalGroupConfig
                    curr={user.groupName}
                    userName={user.username}
                    setter={setNewGroup}
                    title={"그룹 설정"}
                    isOpen={isGroupModalOpen}
                    after={() => { setIsGroupModalOpen(false); }}
                />
            }
            {isModalOpen &&
                <ModalGetString
                    title={"변경할 비밀번호를 입력하세요."}
                    setter={setNewPw}
                    isOpen={isModalOpen}
                    after={() => { setIsModalOpen(false); }}
                />
            }
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text={"사용자 관리"} />
                <div className="searchBox">
                    <input
                        id="searchUser"
                        type="text"
                        placeholder="사용자 ID..."
                        onKeyDown={handleKeyDown}
                        onChange={handleChange} />
                    {searched.length === 0 ? <></> :
                        <div className="userList">
                            {searched.map((item, index) => {
                                return (
                                    <div className="user" key={index} onClick={handleUserSelect}>
                                        <span className="name">{item.name}</span>
                                    </div>
                                )
                            })}
                        </div>}
                </div>
                {user !== null &&
                    <>
                        <div className="profile">
                            <img src={img} alt="프로필 이미지"/>
                            <div className="userinfo">
                                <div className="text">
                                    <span className="title">ID</span>
                                    <span className="value">{user.username}</span>
                                </div>
                                <div className="text">
                                    <span className="title">Nickname</span>
                                    <span className="value">{user.nickname}</span>
                                </div>
                                <div className="text">
                                    <span className="title">Create date</span>
                                    <span className="value">{joined}</span>
                                </div>

                            </div>
                        </div>
                        <TitledBox>
                            <RuleBox
                                title="사용자 계정 비활성화"
                                desc="비활성화된 계정은 로그인이 불가능합니다.">
                                <ToggleBtn onClick={user.locked ? handleActivate : handleDeactivate} default={user.locked} />
                            </RuleBox>
                            <RuleBox
                                title="비밀번호 재설정"
                                desc="비밀번호를 강제로 재설정합니다.">
                                <button className="initBtn" onClick={handlePwInit}>초기화</button>
                            </RuleBox>
                            <RuleBox
                                title="그룹 설정"
                                desc="하나의 그룹에만 속할 수 있습니다.">
                                <span className="currGroup">현재 그룹: {user.groupName === null ? "없음" : user.groupName}</span>
                                <div className="editGroup" onClick={() => setIsGroupModalOpen(true)}><TbEdit/></div>
                            </RuleBox>
                            <RuleBox
                                title="계정 만료일 설정"
                                desc="계정의 만료일을 설정하여 임시 계정으로 전환합니다.">
                                <form className="ruleInput" onSubmit={handleExpire}>
                                    <div className="curr">
                                        <span>현재 만료일: </span>
                                        <span>{user.expiredDate}</span>
                                    </div>
                                    <div className="new">
                                        <span>새 만료일 : </span>
                                        <input type="date" name="newExpireDate"></input>
                                        <input type="submit" value={"적용"} />
                                    </div>
                                </form>
                            </RuleBox>
                        </TitledBox>
                    </>}
            </BodyFrame>
        </>
    )
}
