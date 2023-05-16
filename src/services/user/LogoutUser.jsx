import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { persistor } from "../..";

export default function LogoutUser() {
    const navigate = useNavigate();

    const render = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.alert("로그아웃 되었습니다.");
        navigate("/login");
    }
    const purge = async () => {
        await persistor.purge();
    }
    useEffect(() => {
        render();
        purge();
    }, [])
}