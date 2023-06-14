import React, { useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import ModalOk from "../../component/modal/ModalOk";
import PostBoardAdmin from "../../services/board/PostBoardAdmin";
import BodyHeader from "../../component/main/BodyHeader";

export default function AdminFAQUploadPage() {
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [isOK, setIsok] = useState(false);
    const [message, setMessage] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        inputData.append("boardType", "faq");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") === "" || inputData.get("title") === "") {
            setIsEmpty(true);
            return;
        }
        const res = await PostBoardAdmin(value);
        if (!res[0]) {
            if (typeof res[1] === "object") {
                let tmpMessage = "";
                for (const [, value] of Object.entries(res[1])) {
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
                <BodyHeader text={"FAQ 등록"} />
                <form className="ask" onSubmit={handleSubmit}>
                    <span>{">> FAQ 제목"}</span>
                    <input type="text" name="title" className="title" />
                    <span>{">> FAQ 내용"}</span>
                    <textarea name="content" className="inner" type="text" placeholder="Text..." />
                    <button type="submit" className="askBtn">등록</button>
                </form>
            </BodyFrame>
        </>
    )
}
