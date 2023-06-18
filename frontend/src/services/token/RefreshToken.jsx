import jwtDecode from "jwt-decode";
import { persistor } from "../..";
import SwalError from "../../component/swal/SwalError";

export default async function RefreshToken() {

    if (localStorage.getItem("API_SERVER") === null) {
       localStorage.clear();
       window.location.reload();
    }

    const time = Math.ceil(Date.now() / 1000);
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken !== null || accessToken !== undefined || accessToken !== "" || accessToken !== "null") {
        const accessTokenExp = jwtDecode(accessToken).exp;
        if (accessTokenExp > time) {
            return true;
        }
    }
    if (accessToken === null) {
        SwalError("로그인이 만료되었습니다.");
        persistor.purge();
        window.location.reload();
        return false;        
    }

    const refreshToken = localStorage.getItem("refreshToken");
    const refreshTokenExp = jwtDecode(refreshToken).exp;

    if (refreshTokenExp < time) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        SwalError("로그인이 만료되었습니다.");
        window.location.reload();
        return false;
    }

    const model = {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + refreshToken,
        },
    };

    try {
        const res = await fetch(localStorage.getItem("API_SERVER") + 'auth/refresh', model);
        if (!res.ok) {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            SwalError("로그인이 만료되었습니다.");
            window.location.reload();
        }
        const data = await res.json();
        localStorage.setItem("accessToken", data.result);
        return true;
    } catch (error) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        SwalError("로그인이 만료되었습니다.");
        window.location.reload();
        return false;
    }
}