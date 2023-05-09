import React, { useState } from "react";
import ModalCheckPW from '../../component/modal/ModalCheckPw'

export default function CheckPwRoutes() {
    const [isOpen, setIsOpen] = useState(true);
    const [isSuccess, setIsSuccess] = useState(false);

    return (
        <>
            {isOpen &&
                <ModalCheckPW isOpen={isOpen}
                    after={() => {
                        setIsOpen(false);
                        setIsSuccess(true);
                    }} />
            }

            {/* <Route path='/my/private' element={<PrivatePage />} />
            <Route path='/my/service' element={<ServiceInfoPage />} />
            <Route path='/my/security' element={<SecurityInfoPage />} /> */}
        </>
    )
}
