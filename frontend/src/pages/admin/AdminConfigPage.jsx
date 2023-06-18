import React, { useEffect } from "react";
import { AiOutlineFileUnknown, AiOutlineUserSwitch } from 'react-icons/ai';
import { BsToggleOff, BsToggleOn } from 'react-icons/bs';
import { CgPassword } from 'react-icons/cg';
import BodyFrame from "../../component/Bodyframe";
import RuleBox from "../../component/admin/ruleBox";
import RuleInput from "../../component/admin/ruleInput";
import TitledBox from "../../component/admin/titledBox";
import Header from "../../component/header/Header";
import ModalGetPW from "../../component/modal/ModalGetPW";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import AdminPostConfig from "../../services/config/AdminPostConfig";
import GetConfig from "../../services/config/GetConfig";

export default function AdminConfigPage() {
    const [isAdminNeedChangePassword, setIsAdminNeedChangePassword] = React.useState(false);
    const [configNow, setConfigNow] = React.useState({
        "101": false,
        "102": false,
        "201": false,
        "202": 0,
        "203": 0,
        "204": false,
        "301": 0,
    });

    useEffect(() => {
        [101, 102, 201, 202, 203, 204, 301].forEach(async (code) => {
            const res = await GetConfig(code);
            if(!res[0]){
                SwalError(res[1]);
                return;
            }
            setConfigNow((prev) => {
                return {
                    ...prev,
                    [code]: res[1],
                }
            })
        })
    }, []);

    useEffect(() => {
        const checkPW = async () => {
            const model = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    "id": "admin",
                    "password": "admin",
                }),
            };
            const res = await fetch(localStorage.getItem("API_SERVER") + 'auth/login', model);
            if (res.status === 200) {
                setIsAdminNeedChangePassword(true);
            }
        }
        checkPW();
    }, []);


    const handleToggleSubmit =  ( async (code) => {
        const res = await AdminPostConfig(code, configNow[code] === "true" ? "false" : "true",);
        if(!res[0]){
            SwalError(res[1]);
            return;
        }
        SwalAlert("success", "변경되었습니다.", () => { setConfigNow((prev) => {
            return {
                ...prev,
                [code]: configNow[code] === "true" ? "false" : "true",
            }
         });
        });
    })

    const handleInputSubmit = ( (code, value) => {
        setConfigNow((prev) => {
            return {
                ...prev,
                [code]: value,
            }
        })
    });

    return (
        <>
            {isAdminNeedChangePassword &&
                <ModalGetPW
                    title="관리자 기본 비밀번호 변경 (보안 매우 위험)"
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
                        <div onClick={() => handleToggleSubmit(101)} className="tgBtn">
                            <div className="stateText">{configNow[101] === "true" ? "ON" : "OFF"}</div>
                            {configNow[101] === "true" ? <BsToggleOn /> : <BsToggleOff />}
                        </div>
                    </RuleBox>
                    <RuleBox
                        title="사용자 로그인 ID 변경"
                        desc="사용자가 로그인 ID를 변경할 수 있도록 설정합니다.">
                        <div onClick={() => handleToggleSubmit(102)} className="tgBtn">
                            <div className="stateText">{configNow[102] === "true" ? "ON" : "OFF"}</div>
                            {configNow[102] === "true" ? <BsToggleOn /> : <BsToggleOff />}
                        </div>
                    </RuleBox>
                </TitledBox>
                <TitledBox
                    icon=<CgPassword />
                    text="비밀번호 정책"
                >
                    <RuleBox
                        title="특수문자, 숫자, 알파벳 대문자 조합 사용"
                        desc="비밀번호에 특수문자와 숫자, 알파벳 대문자를 모두 사용하도록 설정합니다.">
                        <div onClick={() => handleToggleSubmit(201)} className="tgBtn">
                            <div className="stateText">{configNow[201] === "true" ? "ON" : "OFF"}</div>
                            {configNow[201] === "true" ? <BsToggleOn /> : <BsToggleOff />}
                        </div>
                    </RuleBox>
                    <RuleBox
                        title="비밀번호 길이 제한"
                        desc="최소 비밀번호 길이를 설정합니다. (무제한: 0)">
                        <RuleInput now={configNow[202]} desc="길이" code="202" onKeyDown={handleInputSubmit} />
                    </RuleBox>
                    <RuleBox
                        title="비밀번호 변경 주기(일)"
                        desc="최대 비밀번호 사용기간을 설정합니다. (무제한: 0)">
                        <RuleInput now={configNow[203]} desc="주기" code="203" onKeyDown={handleInputSubmit} />
                    </RuleBox>
                    <RuleBox
                        title="만료된 비밀번호 계정 차단"
                        desc="ON = 차단, OFF = 경고">
                        <div onClick={() => handleToggleSubmit(204)} className="tgBtn">
                            <div className="stateText">{configNow[204] === "true" ? "ON" : "OFF"}</div>
                            {configNow[204] === "true" ? <BsToggleOn /> : <BsToggleOff />}
                        </div>
                    </RuleBox>
                </TitledBox>
                <TitledBox
                    icon=<AiOutlineFileUnknown />
                    text="파일 정책"
                >
                    <RuleBox
                        title="1인당 업로드 용량"
                        desc="사용자 1인의 최대 업로드 용량 제한을 설정합니다. (무제한: 0)">
                        <RuleInput desc="용량(GB)" now={configNow[301]} code="301" onKeyDown={handleInputSubmit} />
                    </RuleBox>
                </TitledBox>
            </BodyFrame>
        </>
    )
}
