import React from "react";
import { useDispatch, useSelector } from "react-redux";
import Header from "../../component/header/Header";
import ModalCheckPW from '../../component/modal/ModalCheckPw';
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import IsPrivilegedToken from "../../services/token/IsPrivilegedToken";
import { check } from "../../services/token/TokenSlice";

export default function PrivatePage() {
    const isNeedPrivilege = useSelector((state) => state.isPrivileged);
    const dispatch = useDispatch();

    console.log(isNeedPrivilege);
    dispatch(check());
    console.log(isNeedPrivilege);

    return (
        <>
            <Header />
            <SidebarMypage />
            {isNeedPrivilege &&
                <ModalCheckPW
                    isOpen={isNeedPrivilege} />
            }
        </>
    )
}
