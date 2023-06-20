import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from 'react-router-dom';
import SwalAlert from "../../component/swal/SwalAlert";
import SwalError from "../../component/swal/SwalError";
import "../../css/login.css";
import "../../css/modal.css";
import ThrowPingAs from "../../services/log/ThrowPingAs";
import IsAdminToken from "../../services/token/IsAdminToken";
import { asyncCheckAdmin } from "../../slice/TokenSlice";
import { setIsLoggedIn } from "../../slice/UserSlice";
import GetUserPwExpire from "../../services/user/GetUserPwExpire";
import GetConfig from "../../services/config/GetConfig";
import GetLogo from "../../services/config/GetLogo";
import GetName from "../../services/config/GetName";

export default function LoginPage() {

    const [name, setName] = useState();
    const [img, setImg] = useState();
    const [loginInfo, setLoginInfo] = useState({ isSuccess: false, id: '' });
    const navigate = useNavigate();

    const dispath = useDispatch();

    useEffect(() => {
        SwalAlert("success", "본 페이지는 발표용 데모로, 다음 안내 사항을 숙지하시길 부탁드립니다.", () => {
            SwalAlert("warning", "서버에 데이터가 남을 수 있으므로 개인정보는 최대한 남기지 않으시는 것을 추천드립니다. 서버 데이터는 수시로 임의 삭제될 수 있습니다.", () => {
                SwalAlert("info", "또한, 현재 모바일 페이지는 지원하지 않습니다. 모바일 환경에서는 다소 불편한 점이 있을 수 있습니다.", () => {
                    SwalAlert("error", "암호화 및 인코딩은 클라이언트 버전에서만 제공하고 있습니다.", () => {
                        SwalAlert("warning", "서버가 느릴 수 있습니다. 너무 큰 파일의 업로드는 자제 부탁드립니다.", () => {
                            SwalAlert("question", "각종 문의는 '0308bae@gmail.com'으로 부탁드립니다.", () => {
                                SwalAlert("success", "테스트에 참여해주셔서 감사합니다.", () => { });
                            });
                        });
                    });
                });
            });
        });
        localStorage.setItem("API_SERVER", "http://121.155.7.179:8000/");
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

    const afterSuccess = () => {
        dispath(setIsLoggedIn(loginInfo.id));
        setLoginInfo({ isSuccess: false, id: '' });
        navigate('/');
    }

    const dispatch = useDispatch();
    const checkAdmin = async () => {
        const isAdmin = await IsAdminToken();
        if (!isAdmin) { //fail
            return;
        }
        dispatch(asyncCheckAdmin());
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const inputData = new FormData(e.target);
        const value = Object.fromEntries(inputData.entries());
        for (let key in value) { //remove space
            value[key] = value[key].replace(/ /g, "");
        }
        let model = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(value),
        };

        if (inputData.get("id") === "" || inputData.get("password") === "") {
            SwalError("모든 항목을 입력해주세요.");
            return;
        }

        try {
            const res = await fetch(localStorage.getItem("API_SERVER") + 'auth/login', model);
            const data = await res.json();
            if (!res.ok) {
                try {
                    ThrowPingAs("login/" + value.id + "/fail");
                } catch (error) {
                    throw error;
                }
                throw data;
            }
            try {
                const passwordChangeIntervalRes = await GetConfig("203");
                if (!passwordChangeIntervalRes[0]) {
                    throw new Error(passwordChangeIntervalRes[1]);
                }
                const passwordChangeInterval = passwordChangeIntervalRes[1] === null ? 0 : passwordChangeIntervalRes[1];
                if (passwordChangeInterval !== null && passwordChangeInterval !== 0) {

                    const isConfigBlockRes = await GetConfig("204");
                    if (!isConfigBlockRes[0]) {
                        throw new Error(isConfigBlockRes[1]);
                    }
                    const isConfigBlock = isConfigBlockRes[1] === "true" ? "true" : "false";
                    console.log(isConfigBlock);
                    const lastChangedDate = await GetUserPwExpire(value.id, value.password, data.accessToken);
                    if (!lastChangedDate[0]) {
                        throw new Error(lastChangedDate[1]);
                    }

                    const now = new Date();
                    const threeDaysLater = new Date();
                    threeDaysLater.setDate(threeDaysLater.getDate() + 3);
                    const limit = new Date(lastChangedDate[1]);
                    limit.setDate(limit.getDate() + parseInt(passwordChangeInterval));

                    if (limit < now) {
                        if (isConfigBlock === "true") {
                            throw new Error("비밀번호가 만료되었습니다. 비밀번호를 변경해주세요.");
                        }
                        else {
                            localStorage.setItem("accessToken", data.accessToken); //성공
                            localStorage.setItem("refreshToken", data.refreshToken);
                            await checkAdmin();//check admin        

                            setLoginInfo({ isSuccess: true, id: inputData.get("id") });
                            SwalAlert("warning", "비밀번호가 만료일이 초과되었습니다. 보안을 위해 비밀번호를 변경해주세요.", afterSuccess);
                            return;
                        }
                    }
                    else if (limit < threeDaysLater) {
                        localStorage.setItem("accessToken", data.accessToken); //성공
                        localStorage.setItem("refreshToken", data.refreshToken);
                        await checkAdmin();//check admin        

                        setLoginInfo({ isSuccess: true, id: inputData.get("id") });
                        SwalAlert("warning", "3일 이내에 비밀번호가 만료됩니다. (만료 후 : " + (isConfigBlock === "true" ? "계정 차단" : "경고 알림") + ")", afterSuccess);
                        return;
                    }
                }
                ThrowPingAs("login/" + value.id + "/success");
            } catch (error) {
                throw error;
            }


            localStorage.setItem("accessToken", data.accessToken); //성공
            localStorage.setItem("refreshToken", data.refreshToken);
            await checkAdmin();//check admin        

            setLoginInfo({ isSuccess: true, id: inputData.get("id") });
            SwalAlert("success", "로그인 되었습니다.", afterSuccess);
            return;
        } catch (e) {
            SwalError(e.message);
        }
    }
    return (
        <form className="login" onSubmit={handleSubmit}>
            <img src={img} alt="LOGO" />
            <span className="namespan">{name}</span>
            <input name='id' type="text" placeholder="ID" />
            <input name='password' type="password" placeholder="PW" />
            <div className="buttons">
                <Link to='/register' className="link">회원가입</Link>
                <button className="link" >로그인</button>
            </div>
            <Link to='/login/ask'>로그인에 문제가 있으신가요?</Link>
        </form>
    )
}