import React, { useEffect, useState } from "react";
import RuleBox from "../../component/admin/ruleBox";
import TitledBox from "../../component/admin/titledBox";
import BodyFrame from "../../component/bodyframe";
import Header from "../../component/header/header";
import SidebarAdmin from "../../component/sidebar/sidebarAdmin";
import ToggleBtn from "../../component/admin/toggleBtn";
import RuleInput from "../../component/admin/ruleInput";
import default_profile_img from '../../img/defalutProfile.png'
import '../../css/admin.css'


export default function AdminUserCtrl() {

    const [img, setImg] = useState(null);

    const testF = (() => {
        alert("clicked!");
    })

    useEffect(() => {
        if (img === null) {
            setImg(default_profile_img);
        }
    })

    return (
        <>
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <div className="profile">
                    <img src={img} />
                    <div className="userinfo">
                        <div className="text">
                            <span className="title">ID</span>
                            <span className="value">foo</span>
                        </div>
                        <div className="text">
                            <span className="title">Nickname</span>
                            <span className="value">foo</span>
                        </div>
                        <div className="text">
                            <span className="title">Create date</span>
                            <span className="value">1970-01-01</span>
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
            </BodyFrame>
        </>
    )
}
