import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import GetBoardListFrom from "../../services/board/GetBoardListFrom";
import PostBoardAdmin from "../../services/board/PostBoardAdmin";

export default function AdminTermUploadPage() {
    const [isLoading, setIsLoading] = useState(true);
    const [curr, setCurr] = useState();

    useEffect(() => {
        const render = async () => {
            const res = await GetBoardListFrom("inquiries/board/created?boardType=terms&createdDate=0");
            setTimeout(() => setIsLoading(false), 250);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            if (res[1] !== null) {
                setCurr(res[1].content);                
            }
        };
        render();
    }, [])

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        inputData.append("title", "terms");
        inputData.append("boardType", "terms");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") === "") {
            SwalError("내용을 입력해주세요.");
            return;
        }
        const res = await PostBoardAdmin(value);
        if (!res[0]) {
            if (typeof res[1] === "object") {
                let tmpMessage = "";
                for (const [, value] of Object.entries(res[1])) {
                    tmpMessage += value + '\n';
                }
                SwalError(tmpMessage);
            }
            else {
                SwalError(res[1]);
            }
            return;
        }
        SwalAlert("success", "이용약관이 등록되었습니다.", () => window.location.reload());
    }


    return (
        <>
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text={"약관 변경"} />
                <form className="ask" onSubmit={handleSubmit}>
                    <span>{">> 약관"}</span>
                    <textarea name="content" className="inner" type="text" defaultValue={curr}/>
                    <button type="submit" className="askBtn">등록</button>
                </form>
            </BodyFrame>
        </>
    )
}
