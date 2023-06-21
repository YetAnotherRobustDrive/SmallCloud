import React, { useEffect, useState } from "react";
import "../../css/error.css"
import { Link } from "react-router-dom";
import GetLogo from "../../services/config/GetLogo";
import GetName from "../../services/config/GetName";
export default function ErrorPage() {

    const [name, setName] = useState();
    const [img, setImg] = useState();

    useEffect(() => {
        const getLogo = async () => {
            const res = await GetLogo();
            setImg(res);
        }

        const getName = async () => {
            const res = await GetName();
            setName(res);
        }
        getLogo();
        getName();
    }, [])


    return (
        <div className="error">
            <img src={img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <span className="errorspan">접근할 수 없는 페이지입니다.</span>
            <Link className="backhome" to="/">홈으로</Link>
        </div>
    )
}