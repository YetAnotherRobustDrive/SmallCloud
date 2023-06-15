import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import BodyHeader from "../../component/main/BodyHeader";
import ModalLoading from "../../component/modal/ModalLoading";
import SidebarCS from "../../component/sidebar/SidebarCS";
import SwalError from "../../component/swal/SwalError";
import GetBoardListFrom from "../../services/board/GetBoardListFrom";

export default function TermsPage() {
    const [termData, setTermData] = useState("");
    const [privacyData, setPrivacyData] = useState("");
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const render = async () => {
            const termRes = await GetBoardListFrom("inquiries/board/created?boardType=terms&createdDate=0");
            const privRes = await GetBoardListFrom("inquiries/board/created?boardType=privacy&createdDate=0");
            if (!termRes[0] || !privRes[0]) {
                SwalError("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
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
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="이용 약관" />
                <div className="terms">
                    <div className="inner">{termData === null ? "" : termData.content}</div>
                </div>
                <BodyHeader text="개인정보 처리 방침" />
                <div className="terms">
                    <div className="inner">{privacyData === null ? "" : privacyData.content}</div>
                </div>
            </BodyFrame>
        </>
    )
}
