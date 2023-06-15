import React from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import PostBoardAdmin from "../../services/board/PostBoardAdmin";
import SwalError from "../../component/swal/SwalError";
import SwalAlert from "../../component/swal/SwalAlert";

export default function AdminNoticeUploadPage() {

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        inputData.append("boardType", "announcement");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") === "" || inputData.get("title") === "") {
            SwalError("공지사항 제목과 내용을 입력해주세요.");
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
        SwalAlert("success", "공지사항이 등록되었습니다.", () => window.location.reload());
    }


    return (
        <>
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
