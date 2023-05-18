import React from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import SidebarAdmin from "../../component/sidebar/SidebarAdmin";
import CustomNode from "../../services/group/CustomNode";
import CustomRootNode from "../../services/group/CustomRootNode";
import Flow from "../../services/group/Flow";

const nodeTypes = { customNode: CustomNode, customRootNode: CustomRootNode };
export default function AdminGroupConfig() {

    return (
        <>
            <Header />
            <SidebarAdmin />
            <BodyFrame>
                <Flow nodeTypes={nodeTypes}/>
            </BodyFrame>
        </>
    )
}
