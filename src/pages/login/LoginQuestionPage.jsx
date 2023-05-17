import React, { useState } from "react";
import ModalOk from "../../component/modal/ModalOk";
import logo_img from '../../config/img/logo.png'
import "../../css/login.css"
import "../../css/modal.css"
import PostBoard from "../../services/board/PostBoard";
import GetUserInfo from "../../services/user/GetUserInfo";
//todo
export default function LoginQuestionPage() {    
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [isNotCheck, setIsNotCheck] = useState(false);
    const [message, setMessage] = useState("일시적인 오류가 발생했습니다.");

    const handleSubmit = async (e) => {
        e.preventDefault();
        const user = await GetUserInfo();

        const inputData = new FormData(e.target);
        inputData.append("writer", user.nickname);
        const value = Object.fromEntries(inputData.entries());
        if (inputData.get("content") == "" || inputData.get("title") == "" || inputData.get("contact") == "") {
            setIsEmpty(true);
            return;
        }
        else if(inputData.get("check") == null ) {
            setIsNotCheck(true);
            return;
        }
        const res = await PostBoard(value);
        console.log(res);
        if (!res[0]) {
            if (res[1] != undefined) {
                setMessage(res[1]);                
            }
            setMessage("일시적인 오류가 발생했습니다.");      
            setIsFail(true);
            return;
        }
    }

    return (
        <>
        {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
        {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"입력하지 않은 내용이 있습니다."}</ModalOk>}
        {isNotCheck && <ModalOk close={() => setIsNotCheck(false)}>{"개인정보 수집에 동의해주세요."}</ModalOk>}
        <div className="login">
            <img src={logo_img} alt="LOGO" />
            <form className="ask" onSubmit={handleSubmit}>
                <span className="askSpan">관리자에게 문의하기</span>
                <span>{">> 문의 제목"}</span>
                <input type="text" name="title" className="titles" />
                <span>{">> 문의 내용"}</span>
                <textarea name="content" className="askText" type="text" placeholder="Text..." />
                <div className="askAdditional">
                    <div>
                        <span>연락처</span>
                        <input name="contact" className="contact" type="text" placeholder="phone or e-mail" />
                    </div>
                    <div>
                        <span>개인정보 수집 동의</span>
                        <input type="checkbox" className="checkBox" name="check"/>
                    </div>
                    <div className="buttons">
                        <button>문의 등록</button>
                    </div>
                </div>
            </form>
        </div>
        </>
    )
}