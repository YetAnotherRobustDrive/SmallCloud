import React, { useState } from "react";
import Header from "../../component/header/Header"
import BodyFrame from "../../component/Bodyframe";
import SidebarCS from "../../component/sidebar/SidebarCS";
import ExtendBox from "../../component/cs/ExtendBox";
import BodyHeader from "../../component/main/BodyHeader";
import configData from "../../config/config.json"

import datas from "../../fakeJSON/FAQ.json"
import { useSelector } from "react-redux";
import GetUserInfo from "../../services/user/GetUserInfo";
import ModalOk from "../../component/modal/ModalOk";

import '../../css/cs.css'

export default function QuestionPage() {
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [message, setMessage] = useState("일시적인 오류가 발생했습니다.");
    const nickname = useSelector(state => state.user.nickname);

    const handleSubmit = async (e) => {
        e.preventDefault();
        await GetUserInfo();

        const inputData = new FormData(e.target);
        inputData.append("nickname", nickname);

        const value = Object.fromEntries(inputData.entries());
        let model = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(value),
        };

        if (inputData.get("context") == "") {
            setIsEmpty(true);
            return;
        }

        try {//todo
            const res = await fetch(configData.API_SERVER + 'auth/login', model);
            const data = await res.json();
            if (!res.ok) {
                throw data;
            }
            //성공
            window.alert("등록 완료되었습니다!");
            return;
        } catch (e) {
            if (e.message != undefined) setMessage(e.message)
            setIsFail(true);
        }
    }

    return (
        <>

            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"문의 내용을 입력해주세요."}</ModalOk>}
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="1:1 문의하기" />
                <form className="ask" onSubmit={handleSubmit}>
                    <textarea name="context" className="inner" type="text" placeholder="Text..." />
                    <button type="submit" className="askBtn">제출</button>
                </form>
                <BodyHeader text="내 문의 내역" />
                {
                    datas.map((data) => {
                        return <ExtendBox key={data.id} title={data.title}>{data.content}</ExtendBox>
                    })
                }
            </BodyFrame>
        </>
    )
}
