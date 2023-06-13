import React, { useEffect, useState } from "react";
import Header from "../../component/header/Header";
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import BodyFrame from "../../component/Bodyframe";
import BodyHeader from "../../component/main/BodyHeader";
import ReacDiffViewer from "react-diff-viewer";
import GetBoardListFrom from "../../services/board/GetBoardListFrom";

export default function ServiceInfoPage() {

    const [lastTwoTerm, setLastTwoTerm] = useState([]);
    const [lastTwoPrivacy, setLastTwoPrivacy] = useState([]);

    useEffect(() => {
        const renderPrivacy = async () => {
            const privatesNew = await GetBoardListFrom("inquiries/board/created?boardType=privacy&createdDate=0");
            const privatesOld = await GetBoardListFrom("inquiries/board/created?boardType=privacy&createdDate=1");
            if (privatesNew[1] === null) {
                setLastTwoPrivacy(["", ""]);
                return;
            }
            if (privatesOld[1] === null) {
                setLastTwoPrivacy(["", privatesNew[1].content]);
                return;
            }
            setLastTwoPrivacy([privatesOld[1].content, privatesNew[1].content]);
        }
        const renderTerms = async () => {
            const termNew = await GetBoardListFrom("inquiries/board/created?boardType=terms&createdDate=0");
            const termOld = await GetBoardListFrom("inquiries/board/created?boardType=terms&createdDate=1");
            if (termNew[1] === null) {
                setLastTwoTerm(["", ""]);
                return;
            }
            else if (termOld[1] === null) {
                setLastTwoTerm(["", termNew[1].content]);
                return;
            }
            setLastTwoTerm([termOld[1].content, termNew[1].content]);
        }
        renderPrivacy();
        renderTerms();
    }, [])
    
    return (
        <>
            <Header />
            <SidebarMypage />
            <BodyFrame>
                <BodyHeader text="약관 변경내역 (이전 / 최신)" />
                <div style={{width:"100%", overflow:"scroll"}}>
                {(lastTwoTerm[0] === "" || lastTwoTerm[1] === "") ? <div>이전 버전이 없습니다.</div> :
                    <ReacDiffViewer
                        oldValue={lastTwoTerm[0]}
                        newValue={lastTwoTerm[1]}
                        splitView={true}
                    />
                }
                </div>
                <BodyHeader text="개인정보 취급 방침 변경내역 (이전 / 최신)" />
                <div style={{width:"100%", overflow:"scroll"}}>
                {(lastTwoPrivacy[0] === "" || lastTwoPrivacy[1] === "") ? <div>이전 버전이 없습니다.</div> :
                    <ReacDiffViewer
                        oldValue={lastTwoPrivacy[0]}
                        newValue={lastTwoPrivacy[1]}
                        splitView={true}
                    />
                }
                </div>
            </BodyFrame>
        </>
    )
}
