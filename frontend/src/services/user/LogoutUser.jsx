import { useNavigate } from "react-router-dom";
import { persistor } from "../..";
import { useEffect } from "react";
import SwalAlert from "../../component/swal/SwalAlert";

export default function LogoutUser() {
    const navigate = useNavigate();

    const render = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        SwalAlert("success", "로그아웃 되었습니다.");
        navigate("/login");
    }
    const purge = async () => {
        await persistor.purge();
    }

    useEffect(() => {
        render();
        purge();
    }, []);
}