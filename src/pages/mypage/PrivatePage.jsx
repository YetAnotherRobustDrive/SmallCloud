import React, { useEffect, useState } from "react";
import BodyFrame from "../../component/Bodyframe";
import Header from "../../component/header/Header";
import EditableColumn from "../../component/mypage/EditableColumn";
import SidebarMypage from "../../component/sidebar/SidebarMypage";
import default_profile_img from '../../img/defalutProfile.png';
import '../../css/mypage.css'

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
                <div className="private-profile">
                <img src={img}/>
                <EditableColumn
                    title="ID"
                    value="TEST"
                    onSubmit={(e) => { console.log(e) }}
                />
                <EditableColumn
                    title="NAME"
                    value="TEST"
                    onSubmit={(e) => { console.log(e) }}
                />
                <EditableColumn
                    title="PW"
                    value="****"
                    onSubmit={(e) => { console.log(e) }}
                />
                <EditableColumn
                    title="GROUP"
                    value="TEST"
                    editable="false"
                    onSubmit={(e) => { console.log(e) }}
                />
                </div>
            </BodyFrame>
        </>
    )
}
