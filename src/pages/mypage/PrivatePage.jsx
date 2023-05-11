import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Header from "../../component/header/Header";
import ModalCheckPW from '../../component/modal/ModalCheckPw';
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import { selectIsPrivileged, setPrivilege } from "../../slice/UserSlice";
import IsPrivilegedToken from "../../services/token/IsPrivilegedToken";
import EditableColumn from "../../component/mypage/EditableColumn";
import BodyFrame from "../../component/Bodyframe";
import default_profile_img from '../../img/defalutProfile.png';

export default function PrivatePage() {
    const [img, setImg] = useState(null);

    useEffect(() => {
        if (img === null) {
            setImg(default_profile_img);
        }
    }, [])

    return (
        <>
            <Header />
            <SidebarMypage />
            <BodyFrame>
                <img 
                style={{width:"100px"}}
                src={img}/>
                <EditableColumn
                    title="ID"
                    value="TEST"
                    onSubmit={() => { console.log('asdf') }}
                />
            </BodyFrame>
        </>
    )
}
