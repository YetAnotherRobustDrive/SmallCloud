import React, { useState } from "react";
import Header from "../component/header/Header";
import SidebarMypage from "../component/sidebar/SidebarMypage";
import ModalCheckPW from '../component/modal/ModalCheckPw'

export default function Mypage() {
    const [isOpen, setIsOpen] = useState(true);

    return (
        <>
            <Header />
            <SidebarMypage />
            {isOpen &&
                <ModalCheckPW isOpen={isOpen} after={() => setIsOpen(false)} />
            }
        </>
    )
}
