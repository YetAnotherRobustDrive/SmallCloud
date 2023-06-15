import jwtDecode from "jwt-decode";
import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import ExtendBox from "../../component/cs/ExtendBox";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import SidebarCS from "../../component/sidebar/SidebarCS";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import '../../css/cs.css';
import GetBoardListFrom from "../../services/board/GetBoardListFrom";
import PostBoard from "../../services/board/PostBoard";

export default function QuestionPage() {
    const [myQuestion, setMyQuestion] = useState([]);
    const [myQuestionAns, setMyQuestionAns] = useState([]);

    useEffect(() => {
        const fetchMyQuestion = async () => {
            const userName = jwtDecode(localStorage.getItem("accessToken")).sub;
            const res = await GetBoardListFrom('inquiries/myQuestions?writer=' + userName);
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            setMyQuestionAns([]);
            res[1].forEach(async (item) => {
                if (item.answerId !== null) {
                    const answerRes = await GetBoardListFrom('inquiries/search/answer?answerId=' + item.answerId);
                    item.answer = answerRes[1].content;
                    setMyQuestionAns([
                        ...myQuestionAns,
                        {
                            id: item.id,
                            answer: answerRes[1].content
                        }
                    ]);
                }
            });
            setMyQuestion(res[1]);
        }
        fetchMyQuestion();
    }, [myQuestionAns]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userName = jwtDecode(localStorage.getItem("accessToken")).sub;
        const inputData = new FormData(e.target);
        inputData.append("writer", userName);
        inputData.append("contact", "등록된 유저입니다.");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") === "" || inputData.get("title") === "") {
            SwalError("내용을 입력해주세요.");
            return;
        }
        const res = await PostBoard(value);
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
        SwalAlert("success", "문의가 등록되었습니다.", () => window.location.reload());
    }

    return (
        <>
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="1:1 문의하기" />
                <form className="ask" onSubmit={handleSubmit}>
                    <span>{">> 문의 제목"}</span>
                    <input type="text" name="title" className="title" />
                    <span>{">> 문의 내용"}</span>
                    <textarea name="content" className="inner" type="text" placeholder="Text..." />
                    <button type="submit" className="askBtn">제출</button>
                </form>
                <BodyHeader text="내 문의 내역" />
                <div style={{ overflow: "scroll", overflowX: "hidden", height: "calc(100vh - 579px)" }}>
                    {myQuestion.length === 0 ? <div style={{ textAlign: "center", marginTop: "20px" }}>문의 내역이 없습니다.</div> :
                        myQuestion.map((data) => {
                            return <ExtendBox key={data.id} title={data.title}>
                                <>
                                    <span>질문</span>
                                    <div style={{ minWidth: "calc(100% - 20px)", width: "max-content", borderBottom: "1px solid black", padding: "10px", marginBottom: "10px" }}>
                                        {data.content}
                                    </div>
                                    <span>답변</span>
                                    <div style={{ minWidth: "calc(100% - 20px)", width: "max-content", padding: "10px" }}>
                                        {
                                            myQuestionAns.find(elem => elem.id === data.id) === undefined ? "답변이 등록되지 않았습니다." : myQuestionAns.find(elem => elem.id === data.id).answer
                                        }
                                    </div>
                                </>
                            </ExtendBox>
                        })
                    }
                </div>
            </BodyFrame>
        </>
    )
}
