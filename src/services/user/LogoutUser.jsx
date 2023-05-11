import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import LoginPage from "../../pages/login/LoginPage";

export default function LogoutUser() {
    const navigate = useNavigate();

    const render = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.alert("로그아웃 되었습니다.");
        navigate("/login");
    }
    useEffect(() => {
        render();
    }, [])
}