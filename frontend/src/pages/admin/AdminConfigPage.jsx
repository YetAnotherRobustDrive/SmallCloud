import React, { useEffect } from "react";
import { AiOutlineFileUnknown, AiOutlineUserSwitch } from 'react-icons/ai';
import { CgPassword } from 'react-icons/cg';
import BodyFrame from "../../component/Bodyframe";
import RuleBox from "../../component/admin/ruleBox";
import RuleInput from "../../component/admin/ruleInput";
import TitledBox from "../../component/admin/titledBox";
import ToggleBtn from "../../component/admin/toggleBtn";
import Header from "../../component/header/Header";
import ModalGetPW from "../../component/modal/ModalGetPW";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";

export default function AdminConfigPage() {
    const [isAdminNeedChangePassword, setIsAdminNeedChangePassword] = React.useState(false);

    useEffect(() => {
        const checkPW = async () => {
            const model = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    "id": "root",
                    "password": "root",
                }),
            };
            const res = await fetch(localStorage.getItem("API_SERVER") + 'auth/login', model);
            if (res.status === 200) {
                setIsAdminNeedChangePassword(true);
            }
        }
        checkPW();
    }, []);


    const testF = (() => {
        alert("clicked!");
    })

    return (
        <>
            {isAdminNeedChangePassword &&
                <ModalGetPW
                    title="관리자 기본 비밀번호 변경"
                    isOpen={isAdminNeedChangePassword}
                    after={() => setIsAdminNeedChangePassword(false)}
                />}
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <TitledBox
                    icon=<AiOutlineUserSwitch />
                    text="사용자정보 변경 정책"
                >
                    <RuleBox
                        title="사용자 닉네임 변경"
                        desc="사용자가 닉네임을 변경할 수 있도록 설정합니다.">
                        <ToggleBtn onClick={testF} />
                    </RuleBox>
                    <RuleBox
                        title="사용자 로그인 ID 변경"
                        desc="사용자가 로그인 ID를 변경할 수 있도록 설정합니다.">
                        <ToggleBtn onClick={testF} />
                    </RuleBox>
                </TitledBox>
                <TitledBox
                    icon=<CgPassword />
                    text="비밀번호 정책"
                >
                    <RuleBox
                        title="특수문자, 숫자, 알파벳 대문자 조합 사용"
                        desc="비밀번호에 특수문자와 숫자, 알파벳 대문자를 모두 사용하도록 설정합니다.">
                        <ToggleBtn onClick={testF} />
                    </RuleBox>
                    <RuleBox
                        title="비밀번호 길이 제한"
                        desc="최소 비밀번호 길이를 설정합니다. (무제한: 0)">
                        <RuleInput desc="길이" />
                    </RuleBox>
                    <RuleBox
                        title="비밀번호 변경 주기"
                        desc="최대 비밀번호 사용기간을 설정합니다. (무제한: 0)">
                        <RuleInput desc="주기" />
                    </RuleBox>
                    <RuleBox
                        title="만료된 비밀번호 계정 차단"
                        desc="ON = 차단, OFF = 경고">
                        <ToggleBtn onClick={testF} />
                    </RuleBox>
                </TitledBox>
                <TitledBox
                    icon=<AiOutlineFileUnknown />
                    text="파일 정책"
                >
                    <RuleBox
                        title="1인당 업로드 용량"
                        desc="사용자 1인의 최대 업로드 용량 제한을 설정합니다. (무제한: 0)">
                        <RuleInput desc="용량(GB)" />
                    </RuleBox>
                </TitledBox>
            </BodyFrame>
        </>
    )
}
