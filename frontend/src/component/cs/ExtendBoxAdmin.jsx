import React, { useState } from "react";
import { FiChevronDown, FiChevronUp } from 'react-icons/fi';
import '../../css/cs.css';
import PostAnswer from "../../services/board/PostAnswer";
import SwalAlert from "../swal/SwalAlert";
import SwalError from "../swal/SwalError";

export default function ExtendBoxAdmin(props) {
    const [isOpen, setIsOpen] = useState(false);

    const handleClick = () => {
        setIsOpen(!isOpen);
    }
    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        inputData.append("questionId", props.id);
        if (inputData.get("content") === "") {
            SwalError("답변을 입력해주세요.");
            return;
        }
        const value = Object.fromEntries(inputData.entries());
        const res = await PostAnswer(value);
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
        SwalAlert("success", "답변이 등록되었습니다.", () => window.location.reload());
    }

    return (
        <>
            <div className="extendBox" key={props.key}>
                <div className="head" onClick={handleClick}>
                    <span className="title">{props.title}</span>
                    <div className="btn" >{isOpen ? <FiChevronUp /> : <FiChevronDown />}</div>
                </div>
                {isOpen &&
                    <form className="quetionBody" onSubmit={handleSubmit}>
                        <div className="questionHeader">
                            <div>작성자 : {props.writer}</div>
                            <div className="right">연락처 : {props.contact}</div>
                        </div>
                        <div className="child">{props.children}</div>
                        {props.contact !== "등록된 유저입니다." &&
                            <>
                                <textarea name="content" className="child" placeholder="text.." value={"연락처를 통한 답변만 가능합니다."}></textarea>
                                <button>답변완료 처리하기</button>
                            </>
                        }
                        {props.contact === "등록된 유저입니다." && <>
                            <textarea name="content" className="child" placeholder="text.."></textarea>
                            <button>등록하기</button>
                        </>
                        }
                    </form>
                }
            </div>
        </>
    )
}