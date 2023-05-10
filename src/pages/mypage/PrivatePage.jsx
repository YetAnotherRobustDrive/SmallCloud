import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import Header from "../../component/header/Header";
import ModalCheckPW from '../../component/modal/ModalCheckPw';
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import { selectIsPrivileged, setPrivilege } from "../../services/token/TokenSlice";
import IsPrivilegedToken from "../../services/token/IsPrivilegedToken";

export default function PrivatePage() {
    const isPrivilege = useSelector(selectIsPrivileged);
    const dispatch = useDispatch();
    
    const checkPrivilege = async () => {
        const res = await IsPrivilegedToken();
        dispatch(setPrivilege({ res }));
    }

    useEffect(() => {
        checkPrivilege();
    },[])

    return (
        <>
            <Header />
            <SidebarMypage />
            {!isPrivilege &&
                <ModalCheckPW
                isOpen={!isPrivilege} 
                after={() => {
                    checkPrivilege();
                }}/>
            }
        </>
    )
}
