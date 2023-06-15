import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import ExtendBoxAdmin from "../../component/cs/ExtendBoxAdmin";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import SwalError from "../../component/swal/SwalError";
import '../../css/admin.css';
import GetBoardListFrom from "../../services/board/GetBoardListFrom";

export default function AdminQuestionList() {
    const [questions, setQuestions] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const render = async () => {
            const res = await GetBoardListFrom('inquiries/questioned');
            if (!res[0]) {
                SwalError(res[1]);
                return;
            }
            setQuestions(res[1]);
            setTimeout(() => setIsLoading(false), 250);
        }
        render();
    }, [])

    return (
        <>
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
