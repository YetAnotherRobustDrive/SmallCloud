import React from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import SidebarCS from "../../component/sidebar/SidebarCS";
import CustomNode from "../../services/group/CustomNode";
import CustomRootNode from "../../services/group/CustomRootNode";
import FlowReadOnly from "../../services/group/FlowReadOnly";
import BodyHeader from "../../component/main/BodyHeader";

const nodeTypes = { customNode: CustomNode, customRootNode: CustomRootNode };
export default function Groups() {
    return (
        <>
            <Header />
            <SidebarCS />
            <BodyFrame>
                <BodyHeader text="조직도" />
                <FlowReadOnly nodeTypes={nodeTypes} />
            </BodyFrame>
        </>
    )
}