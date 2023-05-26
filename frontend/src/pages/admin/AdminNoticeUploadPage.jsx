import React, { useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalOk from "../../component/modal/ModalOk";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import PostBoardAdmin from "../../services/board/PostBoardAdmin";

export default function AdminNoticeUploadPage() {
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [isOK, setIsok] = useState(false);
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        inputData.append("boardType", "announcement");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") == "" || inputData.get("title") == "") {
            setIsEmpty(true);
            return;
        }
        const res = await PostBoardAdmin(value);
        if (!res[0]) {
            if (typeof res[1] == "object") {
                let tmpMessage = new String();
                for (const [key, value] of Object.entries(res[1])) {
                    tmpMessage += value + '\n';
                }
                setMessage(tmpMessage);
            }
            else {
                setMessage(res[1]);
            }
            setIsFail(true);
            return;
        }
        setIsok(true);
    }


    return (
        <>
            {isOK && <ModalOk close={() => { setIsok(false); window.location.reload(); }}>{"등록되었습니다."}</ModalOk>}
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"제목과 내용을 입력해주세요."}</ModalOk>}
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text={"공지사항 등록"} />
                <form className="ask" onSubmit={handleSubmit}>
                    <span>{">> 공지사항 제목"}</span>
                    <input type="text" name="title" className="title" />
                    <span>{">> 공지사항 내용"}</span>
                    <textarea name="content" className="inner" type="text" placeholder="Text..." />
                    <button type="submit" className="askBtn">등록</button>
                </form>
            </BodyFrame>
        </>
    )
}