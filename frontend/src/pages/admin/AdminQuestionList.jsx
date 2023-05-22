import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import ExtendBox from "../../component/cs/ExtendBox";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalOk from "../../component/modal/ModalOk";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import '../../css/admin.css';
import GetBoardListFrom from "../../services/board/GetBoardListFrom";

export default function AdminQuestionList() {
    const [questions, setQuestions] = useState();
    const [isFail, setIsFail] = useState(false);
    const [message, setMessage] = useState("로그인 에러");

    useEffect(() => {
        const render = async () => {
            const res = await GetBoardListFrom('inquiries');
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]);
                return;
            }
            setQuestions(res[1]);
        }
        render();
    }, [])


    return (
        <>
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text="미확인 문의" />
                {questions == null &&
                    <div>문의가 없습니다.</div>
                }
                {questions != null &&
                    questions.map((d) => {//todo
                            <ExtendBox key={d.id} title={d.title}>
                                <>                                
                                    <div>{d.id}</div>
                                    <div>{d.content}</div>
                                    <div>{d.contact}</div>
                                    <div>{d.boardType}</div>
                                    <div>{d.writer}</div>
                                </>
                            </ExtendBox>
                    })
                }
            </BodyFrame>
        </>
    )
}
