import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalLoading from "../../component/modal/ModalLoading";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import GetBoardListFrom from "../../services/board/GetBoardListFrom";
import PostBoardAdmin from "../../services/board/PostBoardAdmin";
import SwalError from "../../component/swal/SwalError";
import Swal from "sweetalert2";
import SwalAlert from "../../component/swal/SwalAlert";

export default function AdminPrivacyUploadPage() {
    const [isLoading, setIsLoading] = useState(true);
    const [curr, setCurr] = useState();

    useEffect(() => {
        const render = async () => {
            const res = await GetBoardListFrom("inquiries/board/created?boardType=privacy&createdDate=0");
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
        inputData.append("title", "privacy");
        inputData.append("boardType", "privacy");
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
        SwalAlert("success", "개인정보취급방침이 등록되었습니다.", () => window.location.reload());
    }


    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
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
