import React, { useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import ExtendBox from "../../component/cs/ExtendBox";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import SidebarCS from "../../component/sidebar/SidebarCS";

import ModalOk from "../../component/modal/ModalOk";
import datas from "../../fakeJSON/FAQ.json";
import GetUserInfo from "../../services/user/GetUserInfo";

import '../../css/cs.css';
import PostBoard from "../../services/board/PostBoard";

export default function QuestionPage() {
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [message, setMessage] = useState("일시적인 오류가 발생했습니다.");

    const handleSubmit = async (e) => {
        e.preventDefault();
        const user = await GetUserInfo();

        const inputData = new FormData(e.target);
        inputData.append("writer", user.nickname);
        inputData.append("contact", "010-0000-0000");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") == "" || inputData.get("title") == "") {
            setIsEmpty(true);
            return;
        }
        const res = await PostBoard(value);
        console.log(res);
        if (!res[0]) {
            if (res[1] != undefined) {
                setMessage(res[1]);                
            }
            setMessage("일시적인 오류가 발생했습니다.");      
            setIsFail(true);
            return;
        }
    }

    return (
        <>

            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"제목과 내용을 입력해주세요."}</ModalOk>}
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="1:1 문의하기" />
                <form className="ask" onSubmit={handleSubmit}>
                    <span>{">> 문의 제목"}</span>
                    <input type="text" name="title" className="title"/>
                    <span>{">> 문의 내용"}</span>
                    <textarea name="content" className="inner" type="text" placeholder="Text..." />
                    <button type="submit" className="askBtn">제출</button>
                </form>
                <BodyHeader text="내 문의 내역" />
                <div style={{overflow:"scroll", overflowX :"hidden",height:"calc(100vh - 579px)"}}>
                {
                    datas.map((data) => {
                        return <ExtendBox key={data.id} title={data.title}>{data.content}</ExtendBox>
                    })
                }
                </div>
            </BodyFrame>
        </>
    )
}
