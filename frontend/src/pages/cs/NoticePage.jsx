import React from "react";
import Header from "../../component/header/Header"
import BodyFrame from "../../component/Bodyframe";
import SidebarCS from "../../component/sidebar/SidebarCS";
import ExtendBox from "../../component/cs/ExtendBox";

import datas from '../../fakeJSON/notice.json'

export default function NoticePage() {
    return (
        <>
            <Header />
            <SidebarCS />
            <BodyFrame>
                {
                    datas.map((data) => {
                        return <ExtendBox key={data.id} title={data.title}>{data.content}</ExtendBox>
                    })
                }
            </BodyFrame>
        </>
    )
}
