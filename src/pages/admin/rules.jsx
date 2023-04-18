import React from "react";
import BodyFrame from "../../component/bodyframe";
import Header from "../../component/header/header";
import SidebarAdmin from "../../component/sidebar/sidebarAdmin";

export default function AdminRules() {
    return (
        <>
            <Header />
            <SidebarAdmin/>
            <BodyFrame>
                <div>test</div>
            </BodyFrame>
        </>
    )
}
