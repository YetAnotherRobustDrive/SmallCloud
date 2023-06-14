import jwtDecode from "jwt-decode";
import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import ExtendBox from "../../component/cs/ExtendBox";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalOk from "../../component/modal/ModalOk";
import SidebarCS from "../../component/sidebar/SidebarCS";
import '../../css/cs.css';
import GetBoardListFrom from "../../services/board/GetBoardListFrom";
import PostBoard from "../../services/board/PostBoard";

export default function QuestionPage() {
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [isOK, setIsok] = useState(false);
    const [message, setMessage] = useState("일시적인 오류가 발생했습니다.");
    const [myQuestion, setMyQuestion] = useState([]);
    const [myQuestionAns, setMyQuestionAns] = useState([]);

    useEffect(() => {
        const fetchMyQuestion = async () => {
            const userName = jwtDecode(localStorage.getItem("accessToken")).sub;
            const res = await GetBoardListFrom('inquiries/myQuestions?writer=' + userName);
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]);
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
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userName = jwtDecode(localStorage.getItem("accessToken")).sub;
        const inputData = new FormData(e.target);
        inputData.append("writer", userName);
        inputData.append("contact", "등록된 유저입니다.");
        const value = Object.fromEntries(inputData.entries());

        if (inputData.get("content") === "" || inputData.get("title") === "") {
            setIsEmpty(true);
            return;
        }
        const res = await PostBoard(value);
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
            {isOK && <ModalOk close={() => { setIsok(false); window.location.reload(); }}>{"등록되었습니다."}</ModalOk>}
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"제목과 내용을 입력해주세요."}</ModalOk>}
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
                                            myQuestionAns.map((item) => {
                                                if (item.id === data.id) {
                                                    return item.answer;
                                                }
                                            }) === undefined ? "등록된 답변이 없습니다." : myQuestionAns.map((item) => {
                                                if (item.id === data.id) {
                                                    return item.answer;
                                                }
                                            }
                                            )
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
