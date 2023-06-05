import React, { useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import RuleBox from "../../component/admin/ruleBox";
import RuleInput from "../../component/admin/ruleInput";
import TitledBox from "../../component/admin/titledBox";
import ToggleBtn from "../../component/admin/toggleBtn";
import Header from "../../component/header/Header";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import '../../css/admin.css';
import default_profile_img from '../../img/defalutProfile.png';
import AdminGetUserInfo from "../../services/admin/AdminGetUserInfo";
import GetSearchUser from "../../services/user/GetSearchUser";
import BodyHeader from "../../component/main/BodyHeader";


export default function AdminUserCtrlPage() {

    const [img, setImg] = useState(default_profile_img);
    const [user, setUser] = useState(null);
    const [joined, setJoined] = useState(null);
    const [searched, setSearched] = useState([]);

    const testF = (() => {
        alert("clicked!");
    })

    const handleUserSelect = async (e) => {
        const res = await AdminGetUserInfo(e.target.firstChild.innerHTML);
        document.getElementById("searchUser").value = "";
        setSearched([]);
        if (!res[0]) {
            alert("사용자 정보를 불러오는데 실패했습니다.");
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

    return (
        <>
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
                {user === null ? <></> :
                    <>
                        <div className="profile">
                            <img src={img} />
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
                                desc="계정을 비활성화합니다.">
                                <ToggleBtn onClick={testF} />
                            </RuleBox>
                            <RuleBox
                                title="비밀번호 초기화"
                                desc="비밀번호를 초기화합니다.">
                                <button className="initBtn" onClick={testF}>초기화</button>
                            </RuleBox>
                            <RuleBox
                                title="계정 만료일 설정"
                                desc="계정의 만료일을 설정하여 임시 계정으로 전환합니다.">
                                <RuleInput desc="만료일" />
                            </RuleBox>
                        </TitledBox>
                    </>}
            </BodyFrame>
        </>
    )
}
