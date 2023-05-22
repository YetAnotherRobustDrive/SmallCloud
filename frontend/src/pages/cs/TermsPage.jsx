import React from "react";
import Header from "../../component/header/Header"
import BodyFrame from "../../component/Bodyframe";
import SidebarCS from "../../component/sidebar/SidebarCS";
import BodyHeader from "../../component/main/BodyHeader";

export default function TermsPage() {
    return (
        <>
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="이용 약관"/>
                <div className="terms">
                    <div className="inner">test</div>
                </div>
                <BodyHeader text="개인정보 처리 방침"/>
                <div className="terms">
                    <div className="inner">test</div>
                </div>
            </BodyFrame>
        </>
    )
}
