import React, { useState } from "react";
import ModalOk from "../../component/modal/ModalOk";
import logo_img from '../../config/img/logo.png'
import "../../css/login.css"
import "../../css/modal.css"
import PostBoard from "../../services/board/PostBoard";
import GetUserInfo from "../../services/user/GetUserInfo";
import { useNavigate } from "react-router-dom";
//todo
export default function LoginQuestionPage() {    
    const [isEmpty, setIsEmpty] = useState(false);
    const [isFail, setIsFail] = useState(false);
    const [isSuccess, setIsSuccess] = useState(false);
    const [isNotCheck, setIsNotCheck] = useState(false);
    const [isNoticed, setIsNoticed] = useState(false);
    const [message, setMessage] = useState("일시적인 오류가 발생했습니다.");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        if (inputData.get("content") == "" || inputData.get("title") == "" || inputData.get("contact") == "") {
            setIsEmpty(true);
            return;
        }
        else if(inputData.get("check") == null ) {
            setIsNotCheck(true);
            return;
        }
        inputData.delete("check");
        const value = Object.fromEntries(inputData.entries());
        const res = await PostBoard(value);
        if (!res[0]) {
            if (res[1] != undefined) {
                setMessage(res[1]);                
            }
            setMessage("일시적인 오류가 발생했습니다.");      
            setIsFail(true);
            return;
        }
        setIsSuccess(true);
    }

    return (
        <>
        {isSuccess && <ModalOk close={() => {setIsSuccess(false); navigate('/')}}>{"문의가 등록되었습니다.\n등록하신 연락처로 답변드리겠습니다."}</ModalOk>}
        {isFail && <ModalOk close={() => setIsFail(false)}>{message}</ModalOk>}
        {isEmpty && <ModalOk close={() => setIsEmpty(false)}>{"입력하지 않은 내용이 있습니다."}</ModalOk>}
        {isNotCheck && <ModalOk close={() => setIsNotCheck(false)}>{"개인정보 수집에 동의해주세요."}</ModalOk>}
        {isNoticed && <ModalOk>{"이용자가 제공한 모든 정보는 다음의 목적을 위해 활용하며, 하기 목적 이외의 용도로는 사용되지 않습니다.\n\n① 개인정보 수집 항목 및 수집·이용 목적\n가) 수집 항목 (필수항목)\n전화번호(자택, 휴대전화), 이메일\n나) 수집 및 이용 목적\n문의 답변\n\n② 개인정보 보유 및 이용기간\n수집·이용 동의일로부터 개인정보의 수집·이용목적을 달성할 때까지\n\n③ 동의거부관리\n귀하께서는 본 안내에 따른 개인정보 수집, 이용에 대하여 동의를 거부하실 권리가 있습니다. \n다만, 귀하가 개인정보의 수집/이용에 동의를 거부하시는 경우에 서비스 이용이 불가능함을 알려드립니다.\n"}</ModalOk>}
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
                        <input type="checkbox" className="checkBox" name="check" onClick={()=>{setIsNoticed(true)}}/>
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