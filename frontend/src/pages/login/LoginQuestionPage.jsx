import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import "../../css/login.css";
import "../../css/modal.css";
import PostBoard from "../../services/board/PostBoard";
import GetLogo from "../../services/config/GetLogo";

export default function LoginQuestionPage() {
    const navigate = useNavigate();
    const [img, setImg] = useState();

    useEffect(() => {
        const getLogo = async () => {
            const res = await GetLogo();
            setImg(res);
        }
        getLogo();
    }, [])



    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        if (inputData.get("content") === "" || inputData.get("title") === "" || inputData.get("contact") === "") {
            SwalError("모든 항목을 입력해주세요.");
            return;
        }
        else if (inputData.get("check") === null) {
            SwalError("개인정보 수집에 동의해주세요.");
            return;
        }
        inputData.delete("check");
        const value = Object.fromEntries(inputData.entries());
        const res = await PostBoard(value);
        if (!res[0]) {
            if (typeof res[1] === "object") {
                let tmpMessage = "";
                for (const [, value] of Object.entries(res[1])) {
                    tmpMessage += value + '\n';
                }
                SwalError(tmpMessage);
            }
            else {
                SwalError(res[1]);
            }
            return;
        }
        SwalAlert("success", "문의가 등록되었습니다.", () => { navigate("/login"); } );
    }

    return (
        <>
            <div className="login">
                <img src={img} alt="LOGO" />
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
                            <input type="checkbox" className="checkBox" name="check" onClick={() => { 
                                SwalAlert("info", "이용자가 제공한 모든 정보는 다음의 목적을 위해 활용하며, 하기 목적 이외의 용도로는 사용되지 않습니다.\n\n① 개인정보 수집 항목 및 수집·이용 목적\n가) 수집 항목 (필수항목)\n전화번호(자택, 휴대전화), 이메일\n나) 수집 및 이용 목적\n문의 답변\n\n② 개인정보 보유 및 이용기간\n수집·이용 동의일로부터 개인정보의 수집·이용목적을 달성할 때까지\n\n③ 동의거부관리\n귀하께서는 본 안내에 따른 개인정보 수집, 이용에 대하여 동의를 거부하실 권리가 있습니다. \n다만, 귀하가 개인정보의 수집/이용에 동의를 거부하시는 경우에 서비스 이용이 불가능함을 알려드립니다.\n")
                             }} />
                        </div>
                        <div className="buttons">
                            <button>문의 등록</button>
                        </div>
                        <Link to="/">돌아가기</Link>
                    </div>
                </form>
            </div>
        </>
    )
}
