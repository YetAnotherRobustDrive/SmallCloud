import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import ExtendBoxAdmin from "../../component/cs/ExtendBoxAdmin";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalLoading from "../../component/modal/ModalLoading";
import ModalOk from "../../component/modal/ModalOk";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import '../../css/admin.css';
import GetBoardListFrom from "../../services/board/GetBoardListFrom";

export default function AdminQuestionList() {
    const [questions, setQuestions] = useState([]);
    const [isFail, setIsFail] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [message, setMessage] = useState("로그인 에러");

    useEffect(() => {
        const render = async () => {
            const res = await GetBoardListFrom('inquiries/questioned');
            if (!res[0]) {
                setIsFail(true);
                setMessage(res[1]);
                return;
            }
            setQuestions(res[1]);
            setTimeout(() => setIsLoading(false), 1000);
        }
        render();
    }, [])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <BodyHeader text="미확인 문의" />
                {questions.length === 0 &&
                    <div className="login" style={{ paddingTop: "10%" }}>문의가 없습니다.</div>
                }
                {questions.length !== 0 &&
                    questions.map((d) => {
                        return <ExtendBoxAdmin
                            key={d.id}
                            id={d.id}
                            title={d.title}
                            writer={d.writer}
                            contact={d.contact}>
                            <div>{d.content}</div>
                        </ExtendBoxAdmin>
                    })
                }
            </BodyFrame>
        </>
    )
}
