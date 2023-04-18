import React from "react";
import Header from "../header/header";
import SidebarAdmin from "../sidebar/sidebarAdmin";
import '../../css/admin.css'

export default function AdminRules() {
    return (
        <>
            <Header />
            <SidebarAdmin/>
            <div className="bodyframe">
                <div>test</div>
            </div>
        </>
    )
}
