import React, { useState } from "react";
import logo_img from '../../config/img/logo.png'
import "../../css/login.css"
import "../../css/modal.css"

export default function LoginQuestionPage() {

    return (
        <div className="login">
            <img src={logo_img} alt="LOGO" />
            <div className="ask">
                <span className="askSpan">관리자에게 문의하기</span>
                <textarea className="askText" type="text" placeholder="Text..." />
                <div className="askAdditional">
                    <div>
                        <span>연락처</span>
                        <input type="text" placeholder="phone or e-mail"/>
                    </div>
                    <div>
                        <span>개인정보 수집 동의</span>
                        <input type="checkbox" className="checkBox"/>
                    </div>
                    <div className="buttons">
                        <button>문의 등록</button>
                    </div>
                </div>
            </div>
        </div>

    )
}