import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalOk from "../../component/modal/ModalOk";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import PostBoardAdmin from "../../services/board/PostBoardAdmin";
import GetBoardListFrom from "../../services/board/GetBoardListFrom";
import ModalLoading from "../../component/modal/ModalLoading";

export default function AdminPrivacyUploadPage() {
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [isOK, setIsok] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [message, setMessage] = useState("");
    const [curr, setCurr] = useState();

    useEffect(() => {
        const render = async () => {
            const res = await GetBoardListFrom("inquiries/board/created?boardType=privacy&createdDate=0");
            setTimeout(() => setIsLoading(false), 500);
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]);
                return;
            }
            if (res[1].length !== 0) {
                setCurr(res[1].content);                
            }
        };
        render();
    }, [])

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        inputData.append("title", "privacy");
        inputData.append("boardType", "privacy");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") === "") {
            setIsEmpty(true);
            return;
        }
        const res = await PostBoardAdmin(value);
        if (!res[0]) {
            if (typeof res[1] === "object") {
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
            {isLoading && <ModalLoading isOpen={isLoading} />}
            {isOK && <ModalOk close={() => { setIsok(false);}}>{"등록되었습니다."}</ModalOk>}
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"제목과 내용을 입력해주세요."}</ModalOk>}
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text={"개인정보취급방침 변경"} />
                <form className="ask" onSubmit={handleSubmit}>
                    <span>{">> 개인정보취급방침"}</span>
                    <textarea name="content" className="inner" type="text" defaultValue={curr}/>
                    <button type="submit" className="askBtn">등록</button>
                </form>
            </BodyFrame>
        </>
    )
}
