import React from "react";
import { Link } from 'react-router-dom';
import '../../css/fileview.css';
import DeregisterUser from '../../services/user/DeregisterUser';
import ElevateUser from '../../services/user/ElevateUser';

export default function ModalFileview(props) {
    const fileData = props.file;

    async function tmp1() {
        const [isOk, message] = await ElevateUser('qwer');
        console.log(isOk);
        console.log(message);
    }

    async function tmp2() {
        const [isOk, message] = await DeregisterUser();
        console.log(isOk);
        console.log(message);
    }

    return (
        <div className="fileview">
            <div className='fileviewHead'>
                <span className='filename'>nameeeeeeeeeeeeeeeeeeeeeee</span>
                <div className='closeBtn'>열기</div>
            </div>
            <Link to="/login">로그인</Link>
            <button onClick={tmp1}>ElevateUser</button>
            <button onClick={tmp2}>DeregisterUser</button>
            <button onClick={() => props.after()}>닫기</button>
        </div>
    )

}