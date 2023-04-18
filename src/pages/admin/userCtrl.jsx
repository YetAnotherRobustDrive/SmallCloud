import React from "react";
import RuleBox from "../../component/admin/ruleBox";
import TitledBox from "../../component/admin/titledBox";
import BodyFrame from "../../component/bodyframe";
import Header from "../../component/header/header";
import SidebarAdmin from "../../component/sidebar/sidebarAdmin";
import {AiOutlineUserSwitch, AiOutlineFileUnknown} from 'react-icons/ai';
import {CgPassword} from 'react-icons/cg'
import ToggleBtn from "../../component/admin/toggleBtn";
import RuleInput from "../../component/admin/ruleInput";

export default function AdminUserCtrl() {

    const testF = (() => {
        alert("clicked!");
    })

    return (
        <>
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <div>
                    profiles to be shown
                </div>
                <TitledBox>
                    <RuleBox
                        title="사용자 계정 비활성화"
                        desc="계정을 비활성화합니다.">
                        <ToggleBtn onClick={testF}/>
                    </RuleBox>
                    <RuleBox
                        title="비밀번호 초기화"
                        desc="비밀번호를 초기화합니다.">
                        <button style={{border:"none" }} onClick={testF}>초기화</button>
                    </RuleBox>
                    <RuleBox
                        title="계정 만료일 설정"
                        desc="계정의 만료일을 설정하여 임시 계정으로 전환합니다.">
                        <RuleInput desc="만료일"/>
                    </RuleBox>
                </TitledBox>
            </BodyFrame>
        </>
    )
}
