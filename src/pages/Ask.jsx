import React from "react";
import Header from "../component/header/Header"
import BodyFrame from "../component/Bodyframe";
import SidebarCS from "../component/sidebar/SidebarCS";
import ExtendBox from "../component/cs/ExtendBox";
import BodyHeader from "../component/main/BodyHeader";

import datas from "../fakeJSON/FAQ.json"


export default function Ask() {
    return (
        <>
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="1:1 문의하기" />
                <div className="ask">
                    <div className="inner">test</div>
                    <button className="askBtn">제출</button>
                </div>
                <BodyHeader text="내 문의 내역" />
                {
                    datas.map((data) => {
                        return <ExtendBox key={data.id} title={data.title}>{data.content}</ExtendBox>
                    })
                }
            </BodyFrame>
        </>
    )
}
