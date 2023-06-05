import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import ModalLoading from "../../component/modal/ModalLoading";
import ModalOk from "../../component/modal/ModalOk";
import SidebarCS from "../../component/sidebar/SidebarCS";
import GetBoardListFrom from "../../services/board/GetBoardListFrom";
import BodyHeader from "../../component/main/BodyHeader";

export default function TermsPage() {
    const [termData, setTermData] = useState("");
    const [privacyData, setPrivacyData] = useState("");
    const [isFail, setIsFail] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [message, setMessage] = useState("로그인 에러");

    useEffect(() => {
        const render = async () => {
            const termRes = await GetBoardListFrom("inquiries/board/created?boardType=terms&createdDate=0");
            const privRes = await GetBoardListFrom("inquiries/board/created?boardType=privacy&createdDate=0");
            if (!termRes[0] || !privRes[0]) {
                setIsFail(true);
                setMessage(termRes[1]);
                return;
            }
            setTermData(termRes[1]);
            setPrivacyData(privRes[1]);
        };
        render();
        setTimeout(() => setIsLoading(false), 250);
    }, [])

    return (
        <>
            {isLoading && <ModalLoading isOpen={isLoading} />}
            {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="이용 약관" />
                <div className="terms">
                    <div className="inner">{termData.content}</div>
                </div>
                <BodyHeader text="개인정보 처리 방침" />
                <div className="terms">
                    <div className="inner">{privacyData.content}</div>
                </div>
            </BodyFrame>
        </>
    )
}
