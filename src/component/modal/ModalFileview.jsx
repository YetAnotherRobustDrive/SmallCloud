import React, { useState } from "react";
import { Link } from 'react-router-dom';
import '../../css/fileview.css';
import DeregisterUser from '../../services/user/DeregisterUser';
import RefreshToken from "../../services/token/RefreshToken";
import ModalCheckPW from "./ModalCheckPw";

export default function ModalFileview(props) {
    const [test, setTest] = useState(false);
    const fileData = props.file;

    async function tmp1() {
        setTest(true);
    }

    async function tmp2() {
        const [isOk, message] = await DeregisterUser();
        console.log(isOk);
        console.log(message);
    }

    async function tmp3() {
        await RefreshToken();
    }


    return (
        <div className="fileview">
            <div className='fileviewHead'>
                <span className='filename'>nameeeeeeeeeeeeeeeeeeeeeee</span>
                <div className='closeBtn' onClick={() => props.onClick()}>열기</div>
            </div>
            <Link to="/login">로그인</Link>
            <button onClick={tmp1}>ElevateUser</button>
            <button onClick={tmp2}>DeregisterUser</button>
            <button onClick={tmp3}>Refresh</button>
            <button onClick={() => props.after()}>닫기</button>
            {test &&
                <ModalCheckPW isOpen={test} after={() => setTest(false)}/>
            }
        </div>
    )

}